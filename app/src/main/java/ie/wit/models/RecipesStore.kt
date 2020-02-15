package ie.wit.models;

interface RecipesStore {
    fun findAll() : List<RecipesModel>
    fun create(recipes: RecipesModel)
    fun update(recipes: RecipesModel)
    fun delete(recipes: RecipesModel)
}