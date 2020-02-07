package com.github.prgrms.social.api.configure.support;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class PageableImpl implements Pageable {

    private final long offset;

    private final int limit;

    public PageableImpl() {
        this(0,5);
    }

    public PageableImpl(long offset, int limit) {
        checkNotNull(offset, "offset must be provided.");
        checkNotNull(limit, "limit must be provided.");
        checkArgument(
                offset >= 0,
                "offset length must be greater than or equal to zero."
        );
        checkArgument(
                limit >= 1,
                "limit length must be between 1 and 5."
        );

        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public long offset() {
        return offset;
    }

    @Override
    public int limit() {
        return limit;
    }

}