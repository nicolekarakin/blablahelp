package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model;

import javax.validation.Valid;

public record AddressWrap(EAddressType type, @Valid Address address) {
}
