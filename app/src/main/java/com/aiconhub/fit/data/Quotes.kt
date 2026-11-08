package com.aiconhub.fit.data

data class Quote(val text: String, val author: String)

/**
 * Motivational quotes shown on the home screen and after a workout. Every
 * quote is from a public-domain source or is a short, famously attributed
 * quotation, with the attribution kept intact.
 */
object Quotes {
    val all: List<Quote> = listOf(
        Quote("It does not matter how slowly you go as long as you do not stop.", "Confucius"),
        Quote("A journey of a thousand miles begins with a single step.", "Lao Tzu"),
        Quote("No man is free who is not master of himself.", "Epictetus"),
        Quote("First say to yourself what you would be; and then do what you have to do.", "Epictetus"),
        Quote("No great thing is created suddenly.", "Epictetus"),
        Quote("Difficulties strengthen the mind, as labor does the body.", "Seneca"),
        Quote("It is a rough road that leads to the heights of greatness.", "Seneca"),
        Quote("Luck is what happens when preparation meets opportunity.", "Seneca"),
        Quote("Well begun is half done.", "Aristotle"),
        Quote("Knowing yourself is the beginning of all wisdom.", "Aristotle"),
        Quote("The beginning is the most important part of the work.", "Plato"),
        Quote("The first and greatest victory is to conquer yourself.", "Plato"),
        Quote("Health is the greatest of gifts, contentment the best riches.", "Buddha, Dhammapada"),
        Quote("To keep the body in good health is a duty, otherwise we shall not be able to keep our mind strong and clear.", "Buddha"),
        Quote("Though one should conquer a million men in battle, the noblest victory is to conquer oneself.", "Buddha, Dhammapada"),
        Quote("Energy and persistence conquer all things.", "Benjamin Franklin"),
        Quote("Early to bed and early to rise makes a man healthy, wealthy and wise.", "Benjamin Franklin"),
        Quote("The sovereign invigorator of the body is exercise, and of all exercises walking is the best.", "Thomas Jefferson"),
        Quote("Leave all the afternoon for exercise and recreation, which are as necessary as reading.", "Thomas Jefferson"),
        Quote("Strength does not come from physical capacity. It comes from an indomitable will.", "Mahatma Gandhi"),
        Quote("It is health that is real wealth and not pieces of gold and silver.", "Mahatma Gandhi"),
        Quote("You must do the thing you think you cannot do.", "Eleanor Roosevelt"),
        Quote("Whether you think you can or you think you can't, you're right.", "Henry Ford"),
        Quote("I hated every minute of training, but I said: don't quit. Suffer now and live the rest of your life as a champion.", "Muhammad Ali")
    )

    fun forDay(epochDay: Long): Quote {
        val i = (epochDay % all.size).toInt()
        return all[if (i < 0) i + all.size else i]
    }

    fun random(): Quote = all.random()
}
