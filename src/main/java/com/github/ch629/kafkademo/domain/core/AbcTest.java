package com.github.ch629.kafkademo.domain.core;

import org.immutables.value.Value.Immutable;

@Immutable
public abstract class AbcTest extends Test {
    public abstract String getAbc();
}
