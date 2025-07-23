package com.oncomm.oncomm.dto.response;

import com.oncomm.oncomm.domain.model.ClassifiedTransaction;
import com.oncomm.oncomm.domain.model.UnclassifiedTransaction;

import java.util.List;

public record ClassificationPersistenceResult(
        List<ClassifiedTransaction> savedClassified,
        List<UnclassifiedTransaction> savedUnclassified,
        List<ClassifiedTransaction> skippedClassified,
        List<UnclassifiedTransaction> skippedUnclassified
) {}
