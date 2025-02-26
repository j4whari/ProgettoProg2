package borsanova;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Classe concreta <strong>mutabile</strong> che rappresenta un'Azienda quotabile in una o più Borse.
 *
 * <p><strong>Funzione di astrazione (AF)</strong>:
 * Un oggetto {@code Azienda} rappresenta un'entità con un nome univoco (non vuoto)
 * e una collezione di informazioni sulle Borse in cui è quotata
 * (numero di azioni, prezzo iniziale).
 *
 * <p><strong>Invariante di rappresentazione (RI)</strong>:
 * <ul>
 *   <li>{@code name != null && !name.isBlank()}</li>
 *   <li>{@code quotazioni != null}, e né le chiavi (Borsa) né i valori ({@link QuotazioneInfo}) sono null</li>
 *   <li>per ogni borsa in {@code quotazioni},
 *       il numero di azioni e il prezzo sono positivi</li>
 *   <li>Esiste un'unica istanza {@code Azienda} per ciascun nome</li>
 * </ul>
 */
public final class Azienda implements Comparable<Azienda> {

    /**
     * Mappa di nome (stringa) -> istanza univoca di {@link Azienda}.
     * Garantisce l'unicità per nome: la chiave è il nome, e il valore è l'oggetto {@code Azienda}.
     */
    private static final Map<String, Azienda> ISTANZE = new TreeMap<>();

    /**
     * Il nome (non vuoto) di questa azienda.
     */
    private final String name;

    /**
     * Mappa delle Borse in cui è quotata questa azienda,
     * associando a ogni borsa un {@link QuotazioneInfo} (numero di azioni, prezzo iniziale).
     * Non è null, e nessuna chiave/valore è null.
     */
    private final Map<Borsa, QuotazioneInfo> quotazioni = new TreeMap<>();

    /**
     * Struttura dati interna per memorizzare il numero di azioni e il loro prezzo.
     */
    private static class QuotazioneInfo {
        /**
         * Numero di azioni emesse per questa borsa.
         */
        final int numeroAzioni;
        /**
         * Prezzo unitario iniziale in questa borsa.
         */
        final int prezzoUnitario;

        /**
         * Costruttore per le informazioni di quotazione locali.
         *
         * @param numeroAzioni   numero di azioni (&gt; 0 )
         * @param prezzoUnitario prezzo iniziale (&gt; 0 )
         */
        QuotazioneInfo(int numeroAzioni, int prezzoUnitario) {
            if (numeroAzioni <= 0 || prezzoUnitario <= 0) {
                throw new IllegalArgumentException("QuotazioneInfo: parametri invalidi");
            }
            this.numeroAzioni = numeroAzioni;
            this.prezzoUnitario = prezzoUnitario;
        }
    }

    /**
     * Restituisce (o crea) un'istanza univoca di {@code Azienda} col nome specificato.
     *
     * @param name Nome dell'azienda (non vuoto).
     * @return istanza di Azienda.
     * @throws IllegalArgumentException se name è vuoto
     * @throws NullPointerException se name è null
     */
    public static Azienda of(final String name) {
        Objects.requireNonNull(name, "Il nome dell'azienda non può essere null.");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Il nome dell'azienda non può essere vuoto.");
        }
        if (!ISTANZE.containsKey(name)) {
            ISTANZE.put(name, new Azienda(name));
        }
        return ISTANZE.get(name);
    }

    /**
     * Costruttore privato: impedisce istanziazioni dirette e garantisce l'unicità per nome.
     *
     * @param name nome dell'azienda
     */
    private Azienda(final String name) {
        this.name = name;
    }

    /**
     * Restituisce il nome dell'azienda.
     *
     * @return Nome dell'azienda (non vuoto).
     */
    public String getName() {
        return name;
    }

    /**
     * L'azienda si quota in una certa borsa con un certo numero di azioni e prezzo iniziale.
     *
     * <p><strong>Precondizioni</strong>:
     * <ul>
     *   <li>numAzioni &gt; 0</li>
     *   <li>prezzoUnitario &gt; 0</li>
     *   <li>borsa != null</li>
     * </ul>
     *
     * <p><strong>Postcondizioni</strong>:
     * <ul>
     *   <li>L'azienda risulta quotata in tale borsa con le specifiche fornite</li>
     *   <li>Viene chiamato {@code borsa.aggiungiQuotazione(...)} per aggiornare la borsa</li>
     * </ul>
     *
     * @param borsa la Borsa in cui l'azienda viene quotata
     * @param numAzioni numero di azioni emesse
     * @param prezzoUnitario prezzo unitario iniziale
     * @throws IllegalArgumentException se numAzioni o prezzoUnitario sono &lt;= 0
     * @throws NullPointerException se borsa == null
     */
    public void quotaInBorsa(Borsa borsa, int numAzioni, int prezzoUnitario) {
        Objects.requireNonNull(borsa, "La borsa non può essere null.");
        if (numAzioni <= 0 || prezzoUnitario <= 0) {
            throw new IllegalArgumentException("Numero azioni e prezzo devono essere positivi.");
        }
        quotazioni.put(borsa, new QuotazioneInfo(numAzioni, prezzoUnitario));
        borsa.aggiungiQuotazione(this, numAzioni, prezzoUnitario);
    }

    /**
     * Restituisce il prezzo corrente di questa azienda in una data borsa.
     *
     * @param borsa la borsa richiesta
     * @return prezzo corrente dell'azione in tale borsa (o -1 se non quotata).
     * @throws NullPointerException se borsa == null
     * @throws NullPointerException se l'azienda non quotata in tale borsa
     */
    public int getPrezzoCorrente(Borsa borsa) {
        Objects.requireNonNull(borsa, "borsa non può essere null.");
        QuotazioneInfo info = quotazioni.get(borsa);
        Objects.requireNonNull(info, "Azienda non quotata in tale borsa.");
        return borsa.getPrezzoCorrente(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Azienda other)) return false;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Azienda other) {
        return this.name.compareTo(other.name);
    }

}

