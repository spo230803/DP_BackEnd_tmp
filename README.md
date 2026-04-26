# Backend - AF

Informazoni sul utilizzo del Backend

## Vesioni Usate
| Cosa | Versione|
|------|----------|
| Java | 21 LTS |
|Spring Boot | 4.0.0|
|PostrSQL| 15.0 |

## Configurazione
I dati di configurazione sono archivate presso *application.properties* che si divide in due
* **Dev**
  * Usato per i Test
* **prod**
  * Usato per la produzione

### HTTPS
Nella sezione HTTPS si trovanao tutte le inforamzioni per il certificao e configurazione della connessione HTTPS

 |Proprietà| valore | Descrizione                            |
 |---------|--------|----------------------------------------|
 |server.ssl.enabled| ture| Imposta connessione HTTPS              |
 |server.ssl.key-store-type|PKCS12| Tipo di Chiave usata                   |
 | server.ssl.key-store-password|(Password)| Password usata per il Certificato      |
|server.ssl.key-store|classpath:keystore.p12| Posizione dove si trova il Certificato|

### E-Mail
Per l'invio delle E-Mail veien usato un Sever SMPT terzo 

 |Proprietà| valore     | Descrizione                                                       |
 |---------|------------|-------------------------------------------------------------------|
 |spring.mail.host| (Domino)   | Server Mail                                                       |
 |spring.mail.port| 587        | Porta in ascsolto del Server Mail                                 |
 |spring.mail.username| (UserName) | Nome Utente usato per la conensssione al Server Mail              |
 |spring.mail.password| (Password) | Password Usata per la connessione al Server Mail                  |
 |spring.mail.properties.mail.smtp.auth|true| Insica di usare una connessone sicura per il Login al Server Mail |
 |spring.mail.properties.mail.smtp.starttls.enable|true|Insica di usare una connessone sicura per il Login al Server Mail |
 |spring.mail.properties.mail.smtp.timeout|5000| Tempo di attesa del Server Mail|
 |spring.mail.properties.mail.smtp.connectiontimeout|5000|Tempo di attesa prima di chiudere la connessioen al Server|
 |spring.mail.properties.mail.smtp.writetimeout|5000||
|app.mailHtml | false| False = Email inviate in formato testo True = In fomrato HTML|

Invio della Comunicazione è gestito dal servizio *EmailService* e ogni Email ha un "Modello" che viene usato per generae il testo che verrà pasasto alla Service.

### DataBase
Per la Configurazione della connessione al Database. Una parte della configurazione si trova nell file pricipale e 

|Proprietà| valore     | Descrizione                                   |
|---------|------------|-----------------------------------------------|
|spring.datasource.username |(username)| Nome utete di PostrSQL                        |
|spring.datasource.password|(possword)| Password del Utente PostrSQL                  |
|spring.datasource.driver-class-name|org.postgresql.Driver| Dirver di connessione                         |
|spring.datasource.url|jdbc:postgresql://dp_postgres:5433/postgres| Dove si trova il Database e nome del DataBase |
