package ir.alirezaivaz.kartam.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CardsDao {
    @Query("SELECT * FROM cards ORDER BY position ASC")
    suspend fun getAll(): List<CardItem>

    @Query("SELECT * FROM cards WHERE id=:id")
    suspend fun getCard(id: Int): CardItem?

    @Query("SELECT MAX(position) FROM cards")
    suspend fun getMaxPosition(): Int?

    @Insert
    suspend fun insert(cardInfo: CardItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<CardItem>)

    @Update
    suspend fun update(cardInfo: CardItem)

    @Delete
    suspend fun delete(cardInfo: CardItem)
}
