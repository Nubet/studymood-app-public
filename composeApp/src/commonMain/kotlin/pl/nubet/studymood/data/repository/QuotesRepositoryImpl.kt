package pl.nubet.studymood.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.nubet.studymood.domain.model.Quote
import pl.nubet.studymood.domain.model.QuoteCategory

class QuotesRepositoryImpl : QuotesRepository {
    private val _savedQuoteIds = MutableStateFlow<Set<String>>(emptySet())

    private val stressQuotes =
        listOf(
            Quote(
                id = "stress_01",
                text = "It's not stress that kills us, it's our reaction to it.",
                author = "Hans Selye",
                categoryId = "stress",
            ),
            Quote(
                id = "stress_02",
                text =
                    "The greatest weapon against stress is our ability to choose one thought over another.",
                author = "William James",
                categoryId = "stress",
            ),
            Quote(
                id = "stress_03",
                text =
                    "Adopting the right attitude can convert a negative stress into a positive one.",
                author = "Hans Selye",
                categoryId = "stress",
            ),
            Quote(
                id = "stress_04",
                text = "Stress is caused by being 'here' but wanting to be 'there'.",
                author = "Eckhart Tolle",
                categoryId = "stress",
            ),
            Quote(
                id = "stress_05",
                text =
                    "Our anxiety does not come from thinking about the future but from wanting to control it.",
                author = "Amit Ray",
                categoryId = "stress",
            ),
            Quote(
                id = "stress_06",
                text = "It's not the load that breaks you down, it's the way you carry it.",
                author = "Lou Holtz",
                categoryId = "stress",
            ),
            Quote(
                id = "stress_07",
                text =
                    "In times of stress, the best thing we can do for each other is to listen with our ears and our hearts.",
                author = "Fred Rogers",
                categoryId = "stress",
            ),
            Quote(
                id = "stress_08",
                text = "It's not what happens to you, but how you react to it that matters.",
                author = "Epictetus",
                categoryId = "stress",
            ),
            Quote(
                id = "stress_09",
                text = "We suffer more often in imagination than in reality.",
                author = "Seneca",
                categoryId = "stress",
            ),
            Quote(
                id = "stress_10",
                text = "Nothing can bring you peace but yourself.",
                author = "Ralph Waldo Emerson",
                categoryId = "stress",
            ),
            Quote(
                id = "stress_11",
                text = "Difficulties show a person's character.",
                author = "Epictetus",
                categoryId = "stress",
            ),
            Quote(
                id = "stress_12",
                text = "I am not what happened to me, I am what I choose to become.",
                author = "Carl Jung",
                categoryId = "stress",
            ),
            Quote(
                id = "stress_13",
                text = "Pain is inevitable. Suffering is optional.",
                author = "Haruki Murakami",
                categoryId = "stress",
            ),
            Quote(
                id = "stress_14",
                text = "What matters most is how well you walk through the fire.",
                author = "Charles Bukowski",
                categoryId = "stress",
            ),
        )

    private val positivityQuotes =
        listOf(
            Quote(
                id = "pos_01",
                text = "Success is going from failure to failure without losing enthusiasm.",
                author = "Winston Churchill",
                categoryId = "positivity",
            ),
            Quote(
                id = "pos_02",
                text =
                    "There is only one thing that makes a dream impossible to achieve: the fear of failure.",
                author = "Paulo Coelho",
                categoryId = "positivity",
            ),
            Quote(
                id = "pos_03",
                text = "Say something positive, and you'll see something positive.",
                author = "Jim Thompson",
                categoryId = "positivity",
            ),
            Quote(
                id = "pos_04",
                text = "Enjoy all you have while pursuing all you want.",
                author = "Jim Rohn",
                categoryId = "positivity",
            ),
            Quote(
                id = "pos_05",
                text = "Every morning is a fresh start. Wake up with a thankful heart.",
                author = "Kristen Butler",
                categoryId = "positivity",
            ),
            Quote(
                id = "pos_06",
                text = "You are your choices.",
                author = "Lucius Annaeus Seneca",
                categoryId = "positivity",
            ),
            Quote(
                id = "pos_07",
                text = "There is no right time, there is only right now.",
                author = "Mel Robbins",
                categoryId = "positivity",
            ),
            Quote(
                id = "pos_08",
                text = "Even the strongest blizzards start with a single snowflake.",
                author = "Sara Raasch",
                categoryId = "positivity",
            ),
            Quote(
                id = "pos_09",
                text = "Obstacles do not exist to be surrendered to, but only to be broken.",
                author = "Adolf Hitler",
                categoryId = "positivity",
            ),
            Quote(
                id = "pos_10",
                text =
                    "Think thousand times before taking a decision. But after taking decision never turn back even if you get thousand difficulties.",
                author = "Adolf Hitler",
                categoryId = "positivity",
            ),
        )

    private val balanceQuotes =
        listOf(
            Quote(
                id = "bal_01",
                text = "Life is like riding a bicycle. To keep your balance, you must keep moving.",
                author = "Albert Einstein",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_02",
                text =
                    "To put everything in balance is good, to put everything in harmony is better.",
                author = "Victor Hugo",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_03",
                text = "Life is a balance of holding on and letting go.",
                author = "Rumi",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_04",
                text = "Be there for others, but never leave yourself behind.",
                author = "Dodinsky",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_05",
                text = "The art of life is to live in the present moment.",
                author = "Thich Nhat Hanh",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_06",
                text = "You cannot find peace by avoiding life.",
                author = "Virginia Woolf",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_07",
                text = "The quieter you become, the more you are able to hear.",
                author = "Rumi",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_08",
                text =
                    "Happiness is not a matter of intensity but of balance, order, rhythm and harmony.",
                author = "Thomas Merton",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_09",
                text = "Balance is not better time management, but better boundary management.",
                author = "Betsy Jacobson",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_10",
                text =
                    "Self-care is giving the world the best of you, instead of what's left of you.",
                author = "Katie Reed",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_11",
                text = "Never get so busy making a living that you forget to make a life.",
                author = "Dolly Parton",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_12",
                text =
                    "The key is not to prioritize what's on your schedule, but to schedule your priorities.",
                author = "Stephen Covey",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_13",
                text = "Be where you are; otherwise you will miss your life.",
                author = "Buddha",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_14",
                text =
                    "Doing less is not being lazy. Don't give in to a culture that values personal sacrifice over personal productivity.",
                author = "Tim Ferriss",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_15",
                text = "Time you enjoy wasting is not wasted time.",
                author = "Marthe Troly-Curtin",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_16",
                text =
                    "Make the best use of what is in your power, and take the rest as it happens.",
                author = "Epictetus",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_17",
                text = "Life is long, if you know how to use it.",
                author = "Seneca",
                categoryId = "balance",
            ),
            Quote(
                id = "bal_18",
                text =
                    "Everyone thinks of changing the world, but no one thinks of changing himself.",
                author = "Leo Tolstoy",
                categoryId = "balance",
            ),
        )

    override fun getCategories(): List<QuoteCategory> = QuoteCategory.all()

    override fun getQuotesByCategory(categoryId: String): List<Quote> {
        return when (categoryId) {
            "all" -> getAllQuotes()
            "stress" -> stressQuotes
            "positivity" -> positivityQuotes
            "balance" -> balanceQuotes
            else -> emptyList()
        }
    }

    override fun getAllQuotes(): List<Quote> {
        return stressQuotes + positivityQuotes + balanceQuotes
    }

    override fun observeSavedQuotes(): Flow<Set<String>> {
        return _savedQuoteIds.asStateFlow()
    }

    override suspend fun toggleSaveQuote(quoteId: String) {
        _savedQuoteIds.value =
            if (quoteId in _savedQuoteIds.value) {
                _savedQuoteIds.value - quoteId
            } else {
                _savedQuoteIds.value + quoteId
            }
    }

    override fun isSaved(quoteId: String): Boolean {
        return quoteId in _savedQuoteIds.value
    }
}
