package borsanova;

import java.util.Objects;

/**
 * Classe concreta <strong>immutabile</strong> che rappresenta la "vista" di un'azione emessa
 * da una {@link Borsa} per una data {@link Azienda}.
 *
 * <p><strong>Funzione di astrazione (AF)</strong>:
 * Un oggetto {@code Azione} collega un'istanza di {@link Azienda} e una di {@link Borsa},
 * offrendo un metodo per ottenere il prezzo corrente interrogando la borsa.
 *
 * <p><strong>Invariante di rappresentazione (RI)</strong>:
 * <ul>
 *   <li>{@code azienda != null}</li>
 *   <li>{@code borsa != null}</li>
 * </ul>
 */
public final class Azione {

    /**
     * L'azienda associata a questa azione (non null).
     */
    private final Azienda azienda;
    /**
     * La borsa che ha emesso questa azione (non null).
     */
    private final Borsa borsa;

    /**
     * Costruttore di {@code Azione}.
     * <p>
     * Per evitare reference escaping, esegue "copia" ricavando nuovamente
     * l'istanza dal nome (poiché {@link Azienda#of(String)} e {@link Borsa#of(String)}
     * mantengono la unicità).
     *
     * @param azienda l'azienda di riferimento
     * @param borsa   la borsa che ha emesso l'azione
     * @throws NullPointerException se {@code azienda} o {@code borsa} == null
     */
    public Azione(Azienda azienda, Borsa borsa) {
        Objects.requireNonNull(azienda, "L'azienda non può essere null.");
        Objects.requireNonNull(borsa, "La borsa non può essere null.");
        // Costruttore copia: non salviamo direttamente la reference,
        // ma la rigeneriamo dal getName() => of(...)
        this.azienda = Azienda.of(azienda.getName());
        this.borsa   = Borsa.of(borsa.getName());
    }

    /**
     * Restituisce l'azienda associata a questa azione (la "copia" interna).
     *
     * @return l'azienda (non null)
     */
    public Azienda getAzienda() {
        return azienda;
    }

    /**
     * Restituisce la borsa che ha emesso questa azione (la "copia" interna).
     *
     * @return la borsa (non null)
     */
    public Borsa getBorsa() {
        return borsa;
    }

    /**
     * Restituisce il prezzo corrente di questa azione, interrogando la borsa.
     *
     * @return il prezzo corrente
     * @throws NullPointerException se {@code azienda} non quotata in {@code borsa}
     */
    public int getPrezzoCorrente() {
        return borsa.getPrezzoCorrente(azienda);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Azione other)) return false;
        return this.azienda.equals(other.azienda)
                && this.borsa.equals(other.borsa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(azienda, borsa);
    }
}

