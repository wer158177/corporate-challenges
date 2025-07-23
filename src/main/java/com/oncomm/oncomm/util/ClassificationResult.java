package com.oncomm.oncomm.util;

import com.oncomm.oncomm.domain.model.ClassifiedTransaction;
import com.oncomm.oncomm.domain.model.UnclassifiedTransaction;

import java.util.List;

public record ClassificationResult(
        List<ClassifiedTransaction> classified,
        List<UnclassifiedTransaction> unclassified
) {}
