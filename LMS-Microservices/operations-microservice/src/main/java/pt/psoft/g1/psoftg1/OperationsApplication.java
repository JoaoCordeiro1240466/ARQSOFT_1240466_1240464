package pt.psoft.g1.psoftg1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// 👇 ESTAS DUAS LINHAS SÃO CRÍTICAS
@EnableJpaRepositories(basePackages = "pt.psoft.g1.psoftg1")
@EntityScan(basePackages = "pt.psoft.g1.psoftg1")
// 👆 Elas garantem que ele varre TUDO (auth, genre, lending) à procura de Entidades e Repos
@EnableScheduling
public class OperationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OperationsApplication.class, args);
    }
}