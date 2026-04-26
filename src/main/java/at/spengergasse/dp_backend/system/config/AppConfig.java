package at.spengergasse.dp_backend.system.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(UploadProperties.class)
public class AppConfig
{
}//end Class
