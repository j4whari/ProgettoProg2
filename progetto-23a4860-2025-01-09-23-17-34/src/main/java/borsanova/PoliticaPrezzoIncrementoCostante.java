package borsanova;

/**
 * Politica di prezzo ad incremento costante k.
 * <p>
 * Ad ogni acquisto, il prezzo dell'azione aumenta di k unita';
 * resta invariato in caso di vendita.
 *
 * <p><strong>Funzione di astrazione (AF)</strong>:
 * Un oggetto di questa classe rappresenta una politica di prezzo
 * che incrementa il prezzo di un valore k fisso per ogni acquisto.
 *
 * <p><strong>Invariante di rappresentazione (RI)</strong>:
 * <ul>
 *   <li>{@code k >= 0}</li>
 * </ul>
 */
public final class PoliticaPrezzoIncrementoCostante implements PoliticaPrezzo {

    /**
     * Il valore di incremento costante (>= 0).
     */
    private final int k;

    /**
     * Costruttore di politica ad incremento costante.
     *
     * @param k incremento costante non negativo
     * @throws IllegalArgumentException se {@code k < 0}
     */
    public PoliticaPrezzoIncrementoCostante(int k) {
        if (k < 0) {
            throw new IllegalArgumentException("k deve essere >= 0");
        }
        this.k = k;
    }

    @Override
    public int aggiornaPrezzo(int prezzoCorrente, int quantita, boolean acquisto) {
        if (acquisto) {
            return prezzoCorrente + k;
        }
        return prezzoCorrente;
    }
}
