package borsanova;

/**
 * Politica di prezzo a decremento costante k.
 * <p>
 * Ad ogni <strong>vendita</strong>, il prezzo dell'azione diminuisce di k unita';
 * resta invariato in caso di acquisto.
 *
 * <p><strong>Funzione di astrazione (AF)</strong>:
 * Un oggetto di questa classe rappresenta una politica di prezzo
 * che decrementa il prezzo di un valore k fisso solo per vendite.
 *
 * <p><strong>Invariante di rappresentazione (RI)</strong>:
 * <ul>
 *   <li>{@code k >= 0}</li>
 * </ul>
 */
public final class PoliticaPrezzoDecrementoCostante implements PoliticaPrezzo {

    /**
     * Il valore di decremento costante (>= 0).
     */
    private final int k;

    /**
     * Costruttore di politica a decremento costante.
     *
     * @param k decremento costante non negativo
     * @throws IllegalArgumentException se {@code k < 0}
     */
    public PoliticaPrezzoDecrementoCostante(int k) {
        if (k < 0) {
            throw new IllegalArgumentException("k deve essere >= 0");
        }
        this.k = k;
    }

    @Override
    public int aggiornaPrezzo(int prezzoCorrente, int quantita, boolean acquisto) {
        if (!acquisto) {
            // in caso di vendita
            int newPrice = prezzoCorrente - k;
            if (newPrice <= 0) {
                return 1;
            }
            return newPrice;
        }
        // in caso di acquisto
        return prezzoCorrente;
    }
}
