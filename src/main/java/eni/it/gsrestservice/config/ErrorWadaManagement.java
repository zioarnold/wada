package eni.it.gsrestservice.config;

import lombok.Getter;

@Getter
public enum ErrorWadaManagement {
    E_500_INTERNAL_SERVER("Errore interno del server, causa certificati non impostati. Si prega di autenticarsi."),
    E_0001_QMC_ROLE_VALUE_NULL("Campo roles sulla QMC e` vuoto"),
    E_0002_INSERT_GROUP_BY_USER_ID_FAILED("Inserimento del gruppo fallito, contattare assistenza"),
    E_0003_USER_NOT_DELETED("Utente non eliminato, contattare assistenza"),
    E_0004_GROUP_OR_ROLE_NOT_DELETED("Gruppo o ruolo non eliminato, contattare assistenza"),
    E_0005_USER_NOT_UPDATED("Utente non aggiornato, contattare assistenza"),
    E_0006_USERS_NOT_EXISTING_ON_DB("Nessun utente e` presente sul DB, contattare assistenza"),
    E_0007_LDAP_OR_DB_UNAVAILABLE("LDAP o DB non sono raggiungibili, contattare assistenza"),
    E_0008_FILE_IS_EMPTY("File e` vuoto"),
    E_0009_USER_NOT_IN_LDAP("Utente non e` presente nel AD"),
    E_0010_USER_IS_NOT_ON_DB("Utente non e` presente nel DB"),
    E_0011_USERNAME_PASSWORD_INCORRECT("Username o Password non corretti"),
    E_0012_FARM_SELECT_PROBLEM("Non e` stato possibile selezionare la FARM"),
    E_0013_UNABLE_CREATE_ADMIN("Non e` stato possibile creare utenza admin"),
    E_0014_UNABLE_TO_LOGOUT_USER("Non e` stato possibile sloggare utente"),
    E_0015_NOT_AUTHENTICATED("Non e` possibile visualizzare la pagina, senza autenticarsi"),
    E_0016_USER_NOT_EXISTS("Nessun utenza trovata"),
    E_0017_NO_FARM_INSERTED("Nessuna FARM censita"),
    E_0018_NO_REPORT_EXISTS("Nessun report e` presente"),
    E_9999_UNKNOWN_ERROR("Errore imprevisto, contattare assistenza");

    private final String errorMsg;

    ErrorWadaManagement(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
