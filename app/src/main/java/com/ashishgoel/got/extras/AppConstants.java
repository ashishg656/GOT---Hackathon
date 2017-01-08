package com.ashishgoel.got.extras;

public interface AppConstants {

    public final int TYPE_RECYCLER_VIEW_NORMAL = 0;
    public final int TYPE_RECYCLER_VIEW_LOADING = 1;
    public final int TYPE_RECYCLER_VIEW_HEADER = 2;
    public final int TYPE_RECYCLER_VIEW_FOOTER = 3;

    // batter outcomes
    public static final String OUTCOME_WIN = "win";
    public static final String OUTCOME_LOSS = "loss";
    public static final String OUTCOME_DRAW = "draw";

    // database
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GOT.db";

    public interface INTENT_DATA {
        public static final String INTENT_DATA_KING_OBJECT = "INTENT_DATA_KING_OBJECT";
    }

    public interface FRAGMENT_ARGUMENTS {
        public static final String ARGUMENT_ALL_FILTERS = "ARGUMENT_ALL_FILTERS";
        public static final String ARGUMENT_SELECTED_FILTERS = "ARGUMENT_SELECTED_FILTERS";
    }
}
