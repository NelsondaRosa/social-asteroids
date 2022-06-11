package com.ndr.socialasteroids.presentation.payload.response;

import lombok.Data;

@Data
public class Pair<K, V>
{
    private K key;
    private V value;

    public Pair(K key, V value)
    {
        this.key = key;
        this.value = value;
    }
    
    public Pair(){}

    public static <K,V> Pair<K, V> build(K key, V value)
    {
        return new Pair<K, V>(key, value);
    }
}
