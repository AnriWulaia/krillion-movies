package com.krillion.question;

/** The kinds of question TMDB can verify. Each one decides how the valid answers are built. */
public enum RuleType {
    DIRECTOR_FILMOGRAPHY,
    ACTOR_FILMOGRAPHY,
    ACTOR_TV,
    CAST_OF_TITLE,
    GENRE_DECADE,
    STUDIO,
    NETWORK,
    FRANCHISE,
    COMBO_CAST,
    LANGUAGE_GENRE
}
