package org.nnn4eu.hfische.blablahelp.blablahelpbackend.shared.model;

public record Review(String authorName,String message,Long date,boolean isHelpful, boolean isReported) {
}
