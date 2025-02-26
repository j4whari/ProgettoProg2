package borsanova;

/**
 * Interfaccia per una politica di aggiornamento del prezzo di un'azione
 * dopo acquisti o vendite in borsa.
 *
 * <p><strong>Funzione di astrazione (AF)</strong>:
 * Una {@code PoliticaPrezzo} non ha uno stato interno definito a priori (è un'interfaccia),
 * ma rappresenta un criterio di aggiornamento di un prezzo azionario in base a:
 * <ul>
 *   <li>{@code quantita} di azioni scambiate</li>
 *   <li>il tipo di operazione (<em>acquisto</em> o <em>vendita</em>)</li>
 * </ul>
 *
 * <p><strong>Invariante di rappresentazione (RI)</strong>: non applicabile (è un'interfaccia).
 */
public interface PoliticaPrezzo {

    /**
     * Aggiorna il prezzo di un'azione in seguito a un'operazione di acquisto
     * o vendita di {@code quantita} azioni.
     *
     * <p><strong>Precondizioni</strong>:
     * <ul>
     *   <li>{@code prezzoCorrente > 0}: il prezzo deve essere positivo</li>
     *   <li>{@code quantita > 0}: si scambiano almeno 1 azione</li>
     *   <li>{@code acquisto == true} se è un acquisto, {@code false} altrimenti (vendita)</li>
     * </ul>
     *
     * <p><strong>Postcondizioni</strong>:
     * <ul>
     *   <li>Viene restituito un valore di prezzo coerente con l’implementazione concreta
     *       della politica, che potrà essere maggiore, minore o uguale a {@code prezzoCorrente}
     *       a seconda di {@code acquisto} e {@code quantita}.</li>
     *   <li>In caso di risultato minore o uguale a 0 (se la politica lo prevede), la classe
     *       concreta deve garantirne la gestione (es. riportare a 1, oppure non scendere sotto 0).</li>
     * </ul>
     *
     * @param prezzoCorrente prezzo corrente prima dell'aggiornamento
     * @param quantita numero di azioni scambiate
     * @param acquisto true se acquisto, false se vendita
     * @return il nuovo prezzo, coerente con la politica adottata
     */
    int aggiornaPrezzo(int prezzoCorrente, int quantita, boolean acquisto);
}
