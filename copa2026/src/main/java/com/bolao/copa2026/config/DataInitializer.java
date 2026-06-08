package com.bolao.copa2026.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bolao.copa2026.model.Usuario;
import com.bolao.copa2026.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        criarAdministradorPadrao();
    }

    private void criarAdministradorPadrao() {
        String emailAdmin = "romero.buarque@gmail.com";

        if (usuarioRepository.existsByEmail(emailAdmin)) {
            Usuario usuario = usuarioRepository.findByEmail(emailAdmin).orElseThrow();

            usuario.setNome("Romero Buarque");
            usuario.setSenha(passwordEncoder.encode("anna123."));
            usuario.setAdministrador(true);

            usuarioRepository.save(usuario);
            return;
        }

        Usuario admin = new Usuario();

        admin.setNome("Romero Buarque");
        admin.setEmail(emailAdmin);
        admin.setSenha(passwordEncoder.encode("anna123."));
        admin.setTelefone("5581999999999");
        admin.setReceberNotificacaoEmail(true);
        admin.setReceberNotificacaoWhatsapp(true);
        admin.setAdministrador(true);

        usuarioRepository.save(admin);
    }
}