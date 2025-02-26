package borsanova;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Classe concreta <strong>mutabile</strong> che rappresenta un Operatore di borsa.
 *
 * <p><strong>Funzione di astrazione (AF)</strong>:
 * Un oggetto {@code Operatore} rappresenta un individuo/entità con:
 * <ul>
 *   <li>un nome (non vuoto)</li>
 *   <li>un budget (non negativo)</li>
 * </ul>
 *
 * <p><strong>Invariante di rappresentazione (RI)</strong>:
 * <ul>
 *   <li>{@code name != null && !name.isBlank()}</li>
 *   <li>{@code budget >= 0}</li>
 *   <li>Esiste un'unica istanza {@code Operatore} per ogni nome</li>
 * </ul>
 */
public final class Operatore implements Comparable<Operatore> {

    /**
     * Mappa di nome (stringa) -> istanza univoca di {@link Operatore}.
     * Garantisce l'unicità per nome: la chiave è il nome, il valore l'operatore.
     */
    private static final Map<String, Operatore> ISTANZE = new TreeMap<>();

    /**
     * Il nome univoco di questo operatore (non vuoto).
     */
    private final String name;

    /**
     * Budget disponibile (>= 0) per questo operatore.
     */
    private int budget;

    /**
     * Restituisce (o crea) un'istanza univoca di Operatore col nome specificato.
     *
     * <p><strong>Precondizioni</strong>:
     * <ul>
     *   <li>{@code name != null && !name.isBlank()}</li>
     * </ul>
     *
     * @param name il nome (non vuoto)
     * @return l'Operatore corrispondente
     * @throws IllegalArgumentException se {@code name} è vuoto
     * @throws NullPointerException se {@code name} è null
     */
    public static Operatore of(final String name) {
        Objects.requireNonNull(name, "Il nome dell'operatore non può essere null.");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Il nome dell'operatore non può essere vuoto.");
        }
        if (!ISTANZE.containsKey(name)) {
            ISTANZE.put(name, new Operatore(name));
        }
        return ISTANZE.get(name);
    }

    /**
     * Costruttore privato, inizialmente con budget=0.
     *
     * @param name nome dell'operatore
     */
    private Operatore(String name) {
        this.name = name;
        this.budget = 0; // inizialmente 0
    }

    /**
     * Restituisce il nome dell'operatore.
     *
     * @return il nome (non vuoto).
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce il budget corrente di questo operatore.
     *
     * @return il budget (>= 0).
     */
    public int getBudget() {
        return budget;
    }

    /**
     * Deposita una somma nel budget dell'operatore.
     *
     * <p><strong>Precondizioni</strong>:
     * <ul>
     *   <li>{@code amount > 0}</li>
     * </ul>
     *
     * @param amount la somma da depositare
     * @throws IllegalArgumentException se {@code amount <= 0}
     */
    public void deposita(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("L'importo da depositare deve essere positivo.");
        }
        budget += amount;
    }

    /**
     * Preleva una somma dal budget dell'operatore (mai deve diventare negativa).
     *
     * <p><strong>Precondizioni</strong>:
     * <ul>
     *   <li>{@code amount > 0}</li>
     *   <li>{@code budget >= amount}</li>
     * </ul>
     *
     * @param amount la somma da prelevare
     * @throws IllegalArgumentException se {@code amount <= 0}
     * @throws IllegalStateException se {@code budget < amount}
     */
    public void preleva(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("L'importo da prelevare deve essere positivo.");
        }
        if (budget < amount) {
            throw new IllegalStateException("Budget insufficiente per prelevare l'importo richiesto.");
        }
        budget -= amount;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Operatore other)) return false;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Operatore other) {
        return this.name.compareTo(other.name);
    }
}

