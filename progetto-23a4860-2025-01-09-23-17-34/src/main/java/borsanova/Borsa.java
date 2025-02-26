package borsanova;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Classe concreta <strong>mutabile</strong> che rappresenta una Borsa,
 * in cui è possibile quotare {@link Azienda} e scambiare azioni secondo
 * una {@link PoliticaPrezzo}.
 *
 * <p><strong>Funzione di astrazione (AF)</strong>:
 * Un oggetto {@code Borsa} è identificato da un nome univoco {@code name},
 * con un insieme di aziende quotate, ciascuna associata a un prezzo corrente e un numero
 * di azioni disponibili, e una struttura di allocazioni (quante azioni di una data azienda
 * possiede ogni operatore). Ciascuna borsa ha una {@code PoliticaPrezzo} che aggiorna il
 * prezzo delle azioni in seguito agli scambi.
 *
 * <p><strong>Invariante di rappresentazione (RI)</strong>:
 * <ul>
 *   <li>{@code name != null && !name.isBlank()}</li>
 *   <li>{@code politicaPrezzo != null}</li>
 *   <li>Le quantità di azioni (disponibili o allocate) sono sempre >= 0.</li>
 *   <li>{@code azioniInListino != null}, e nessuna chiave/valore è null</li>
 *   <li>{@code allocazioni != null}, e nessuna chiave è null (i {@link AllocKey}) né alcun valore {@code Integer} è null</li>
 * </ul>
 */
public final class Borsa implements Comparable<Borsa> {

    /**
     * Mappa di nome (stringa) -> istanza univoca di {@link Borsa}.
     * Garantisce l'unicità per nome: la chiave è il nome, il valore è la borsa.
     */
    private static final Map<String, Borsa> ISTANZE = new TreeMap<>();

    /**
     * Il nome univoco di questa borsa (non vuoto).
     */
    private final String name;

    /**
     * Mappa di {@link Azienda} -> {@link AzioneInfo}, dove {@code AzioneInfo}
     * contiene il prezzoCorrente e il numero di azioni disponibili.
     * Non è null; le chiavi e i valori non sono null.
     */
    private final Map<Azienda, AzioneInfo> azioniInListino = new TreeMap<>();

    /**
     * Mappa di {@code AllocKey} -> numero di azioni possedute dall'operatore
     * per una determinata azienda.
     * Non è null; le chiavi e i valori non sono null (valori di tipo {@code Integer}).
     */
    private final Map<AllocKey, Integer> allocazioni = new TreeMap<>();

    /**
     * Politica di prezzo in vigore per questa borsa (non null).
     */
    private PoliticaPrezzo politicaPrezzo;

    /**
     * Struttura dati per tracciare le informazioni di un'azione di una data Azienda
     * in questa Borsa.
     */
    private static class AzioneInfo {
        /**
         * Il prezzo corrente dell'azienda in questa borsa.
         */
        int prezzoCorrente;
        /**
         * Numero di azioni ancora disponibili (non allocate) per questa azienda.
         */
        int azioniDisponibili;

        /**
         * Crea una {@code AzioneInfo} con {@code prezzoCorrente} e {@code azioniDisponibili}.
         *
         * @param prezzo     prezzo corrente iniziale (deve essere &gt;= 0)
         * @param disponibili quante azioni sono disponibili (deve essere &gt;= 0)
         */
        AzioneInfo(int prezzo, int disponibili) {
            if (prezzo < 0 || disponibili < 0) {
                throw new IllegalArgumentException("AzioneInfo: prezzo o disponibili negativi");
            }
            this.prezzoCorrente = prezzo;
            this.azioniDisponibili = disponibili;
        }
    }

    /**
     * Classe-chiave per memorizzare la coppia (Azienda, Operatore).
     */
    private static class AllocKey implements Comparable<AllocKey> {
        /**
         * L'azienda interessata (non null).
         */
        private final Azienda azienda;
        /**
         * L'operatore che possiede (o può possedere) azioni di quell'azienda (non null).
         */
        private final Operatore operatore;

        /**
         * Costruttore di chiave-allocazione.
         *
         * @param azienda   l'azienda (non null)
         * @param operatore l'operatore (non null)
         */
        AllocKey(Azienda azienda, Operatore operatore) {
            Objects.requireNonNull(azienda, "AllocKey: azienda null");
            Objects.requireNonNull(operatore, "AllocKey: operatore null");
            this.azienda = azienda;
            this.operatore = operatore;
        }

        @Override
        public int compareTo(AllocKey o) {
            int cmp = this.azienda.compareTo(o.azienda);
            if (cmp != 0) return cmp;
            return this.operatore.compareTo(o.operatore);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof AllocKey other)) return false;
            return this.azienda.equals(other.azienda)
                    && this.operatore.equals(other.operatore);
        }

        @Override
        public int hashCode() {
            return Objects.hash(azienda, operatore);
        }
    }

    /**
     * Restituisce (o crea, se non esiste) un'istanza univoca di Borsa col nome specificato.
     *
     * @param name nome della borsa (non vuoto)
     * @return la Borsa univoca con tale nome
     * @throws IllegalArgumentException se {@code name} è vuoto
     * @throws NullPointerException se {@code name} è null
     */
    public static Borsa of(final String name) {
        Objects.requireNonNull(name, "Il nome della borsa non può essere null.");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Il nome della borsa non può essere vuoto.");
        }
        if (!ISTANZE.containsKey(name)) {
            ISTANZE.put(name, new Borsa(name));
        }
        return ISTANZE.get(name);
    }

    /**
     * Costruttore privato, con politica di prezzo di default (incremento costante = 0).
     *
     * @param name nome della borsa (non vuoto)
     */
    private Borsa(final String name) {
        this.name = name;
        // Politica di default => PoliticaPrezzoIncrementoCostante(0)
        this.politicaPrezzo = new PoliticaPrezzoIncrementoCostante(0);
    }

    /**
     * Imposta la politica di prezzo da usare in questa Borsa.
     *
     * <p><strong>Precondizioni</strong>:
     * <ul>
     *   <li>{@code politica != null}</li>
     * </ul>
     *
     * @param politica la nuova politica di prezzo
     * @throws NullPointerException se {@code politica == null}
     */
    public void setPoliticaPrezzo(PoliticaPrezzo politica) {
        Objects.requireNonNull(politica);
        this.politicaPrezzo = politica;
    }

    /**
     * Aggiunge le informazioni di quotazione di un'azienda in questa borsa.
     * <p><strong>Precondizioni</strong>:
     * <ul>
     *   <li>{@code azienda != null}</li>
     *   <li>{@code numAzioni >= 0}</li>
     *   <li>{@code prezzoUnitario >= 0}</li>
     * </ul>
     *
     * @param azienda        l'azienda quotata
     * @param numAzioni      quantità di azioni disponibili
     * @param prezzoUnitario prezzo iniziale
     */
    public void aggiungiQuotazione(Azienda azienda, int numAzioni, int prezzoUnitario) {
        Objects.requireNonNull(azienda, "aggiungiQuotazione: azienda null");
        if (numAzioni < 0 || prezzoUnitario < 0) {
            throw new IllegalArgumentException("numAzioni/prezzoUnitario non possono essere negativi");
        }
        azioniInListino.put(azienda, new AzioneInfo(prezzoUnitario, numAzioni));
    }

    /**
     * Restituisce il prezzo corrente di un'azione di una certa Azienda quotata qui.
     * <p><strong>Precondizioni</strong>:
     * <ul>
     *   <li>{@code azienda != null}</li>
     * </ul>
     *
     * @param azienda l'azienda interessata (non null)
     * @return prezzo corrente
     * @throws IllegalArgumentException se {@code azienda} non appartiene al listino
     * @throws NullPointerException se {@code azienda == null}      aggiunto dopo
     */
    public int getPrezzoCorrente(Azienda azienda) {
        Objects.requireNonNull(azienda, "getPrezzoCorrente: azienda null");
        AzioneInfo info = azioniInListino.get(azienda);
        if (info == null) {
            throw new IllegalArgumentException("L'azienda non è presente nel listino.");
        }
        return info.prezzoCorrente;
    }

    /**
     * Restituisce quante azioni di una certa azienda possiede un certo operatore in questa borsa.
     * <p><strong>Precondizioni</strong>:
     * <ul>
     *   <li>{@code operatore != null}</li>
     *   <li>{@code azienda != null}</li>
     * </ul>
     *
     * @param operatore l'operatore (non null)
     * @param azienda   l'azienda (non null)
     * @return numero di azioni, 0 se nessuna
     * @throws NullPointerException se {@code operatore} o {@code azienda} sono null
     */
    public int quantePossedute(Operatore operatore, Azienda azienda) {
        Objects.requireNonNull(operatore, "quantePossedute: operatore null");
        Objects.requireNonNull(azienda, "quantePossedute: azienda null");
        AllocKey key = new AllocKey(azienda, operatore);
        return allocazioni.getOrDefault(key, 0);
    }

    /**
     * Restituisce quante azioni di una certa azienda rimangono disponibili (non allocate) in questa borsa.
     * <p><strong>Precondizioni</strong>:
     * <ul>
     *   <li>{@code azienda != null}</li>
     * </ul>
     *
     * @param azienda l'azienda (non null)
     * @return numero di azioni disponibili, o 0 se non è quotata
     */
    public int getRimanenti(Azienda azienda) {
        Objects.requireNonNull(azienda, "getRimanenti: azienda null");
        AzioneInfo info = azioniInListino.get(azienda);
        if (info == null) return 0;
        return info.azioniDisponibili;
    }

    /**
     * Esegue l'acquisto di {@code nAzioni}, senza acquisto parziale.
     *
     * <p><strong>Precondizioni</strong>:
     * <ul>
     *   <li>{@code operatore != null}</li>
     *   <li>{@code azienda != null}</li>
     *   <li>{@code nAzioni > 0}</li>
     *   <li>L'azienda deve essere quotata in questa borsa</li>
     *   <li>Deve esserci un numero di azioni disponibili &gt;= {@code nAzioni}</li>
     * </ul>
     *
     * @param operatore l'operatore che acquista
     * @param azienda   l'azienda di cui si acquistano azioni
     * @param nAzioni   numero di azioni da acquistare (positivo)
     * @return costo totale dell'operazione
     * @throws NullPointerException se {@code operatore} o {@code azienda} sono null
     * @throws IllegalArgumentException se {@code nAzioni &lt;= 0} o se l'azienda non è quotata
     * @throws IllegalStateException se non ci sono abbastanza azioni disponibili
     */
    public int acquista(Operatore operatore, Azienda azienda, int nAzioni) {
        Objects.requireNonNull(operatore, "Operatore nullo.");
        Objects.requireNonNull(azienda, "Azienda nulla.");
        if (nAzioni <= 0) {
            throw new IllegalArgumentException("Il numero di azioni da acquistare deve essere positivo.");
        }
        AzioneInfo info = azioniInListino.get(azienda);
        if (info == null) {
            throw new IllegalArgumentException("Azienda non quotata in questa borsa.");
        }
        if (info.azioniDisponibili < nAzioni) {
            throw new IllegalStateException("Non ci sono abbastanza azioni disponibili per l'acquisto.");
        }

        int costo = info.prezzoCorrente * nAzioni;
        info.azioniDisponibili -= nAzioni;

        AllocKey key = new AllocKey(azienda, operatore);
        allocazioni.put(key, allocazioni.getOrDefault(key, 0) + nAzioni);

        // Aggiorno il prezzo
        info.prezzoCorrente = politicaPrezzo.aggiornaPrezzo(info.prezzoCorrente, nAzioni, true);

        return costo;
    }

    /**
     * Esegue la vendita di {@code nAzioni}, senza vendita parziale.
     *
     * <p><strong>Precondizioni</strong>:
     * <ul>
     *   <li>{@code operatore != null}</li>
     *   <li>{@code azienda != null}</li>
     *   <li>{@code nAzioni &gt; 0}</li>
     *   <li>L'operatore deve possedere almeno {@code nAzioni} di tale azienda</li>
     * </ul>
     *
     * @param operatore l'operatore che vende
     * @param azienda   l'azienda di cui si vendono azioni
     * @param nAzioni   numero di azioni da vendere (positivo)
     * @return ricavo totale dell'operazione
     * @throws NullPointerException se {@code operatore} o {@code azienda} sono null
     * @throws IllegalArgumentException se {@code nAzioni &lt;= 0} o se l'azienda non è quotata
     * @throws IllegalStateException se l'operatore non possiede abbastanza azioni
     */
    public int vendi(Operatore operatore, Azienda azienda, int nAzioni) {
        Objects.requireNonNull(operatore, "Operatore nullo.");
        Objects.requireNonNull(azienda, "Azienda nulla.");
        if (nAzioni <= 0) {
            throw new IllegalArgumentException("Il numero di azioni da vendere deve essere positivo.");
        }
        AzioneInfo info = azioniInListino.get(azienda);
        if (info == null) {
            throw new IllegalArgumentException("Azienda non quotata in questa borsa.");
        }

        AllocKey key = new AllocKey(azienda, operatore);
        int possedute = allocazioni.getOrDefault(key, 0);
        if (possedute < nAzioni) {
            throw new IllegalStateException("L'operatore non possiede abbastanza azioni da vendere.");
        }

        int ricavo = info.prezzoCorrente * nAzioni;
        info.azioniDisponibili += nAzioni;
        allocazioni.put(key, possedute - nAzioni);

        // Aggiorno il prezzo
        info.prezzoCorrente = politicaPrezzo.aggiornaPrezzo(info.prezzoCorrente, nAzioni, false);

        return ricavo;
    }

    /**
     * Restituisce il nome di questa borsa (non vuoto).
     *
     * @return il nome della borsa
     */
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Borsa other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Borsa other)) return false;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

