package com.example.foodplanner.features.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@JsonAdapter(value = Meal.Deserializer.class)
public class Meal implements Parcelable {

    private String id;
    private String name;
    private String thumbnail;
    private String category;
    private String area;
    private String instructions;
    private List<String> tags;
    private String youtube;
    private String source;
    private List<Ingredient> ingredients;

    public Meal() { }

    public Meal(String id, String name, String thumbnail, String category, String area,
                String instructions, List<String> tags, String youtube, String source,
                List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
        this.tags = tags;
        this.youtube = youtube;
        this.source = source;
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public static class Ingredient implements Parcelable {
        private final String ingredient;
        private final String measure;

        public Ingredient(String ingredient, String measure) {
            this.ingredient = ingredient;
            this.measure = measure;
        }

        protected Ingredient(Parcel in) {
            ingredient = in.readString();
            measure = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(ingredient);
            dest.writeString(measure);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
            @Override
            public Ingredient createFromParcel(Parcel in) {
                return new Ingredient(in);
            }

            @Override
            public Ingredient[] newArray(int size) {
                return new Ingredient[size];
            }
        };

        public String getIngredient() {
            return ingredient;
        }

        public String getMeasure() {
            return measure;
        }
    }

    static final class Deserializer implements JsonDeserializer<Meal> {
        @Override
        public Meal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject root = json.getAsJsonObject();
            return new Meal(
                    root.get("idMeal").getAsString(),
                    root.get("strMeal").getAsString(),
                    root.get("strMealThumb").getAsString(),
                    root.get("strCategory").getAsString(),
                    root.get("strArea").getAsString(),
                    root.get("strInstructions").getAsString(),
                    Arrays.asList(root.get("strTags").getAsString().split(",")),
                    root.get("strYoutube").getAsString(),
                    root.get("strSource").getAsString(),
                    readIngredients(root)
            );
        }

        private List<Ingredient> readIngredients(JsonObject root) {
            List<Ingredient> ingredients = new LinkedList<>();
            for (String ingredientKey : root.keySet()) {
                if (ingredientKey.startsWith("strIngredient")) {
                    String value = root.get(ingredientKey).getAsString();
                    if (value != null && !value.isEmpty()) {
                        ingredients.add(new Ingredient(
                                value,
                                root.get(ingredientKey.replace("strIngredient", "strMeasure")).getAsString()
                        ));
                    }
                }
            }
            return new ArrayList<>(ingredients);
        }
    }

    protected Meal(Parcel in) {
        id = in.readString();
        name = in.readString();
        thumbnail = in.readString();
        category = in.readString();
        area = in.readString();
        instructions = in.readString();
        tags = in.createStringArrayList();
        youtube = in.readString();
        source = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(thumbnail);
        dest.writeString(category);
        dest.writeString(area);
        dest.writeString(instructions);
        dest.writeStringList(tags);
        dest.writeString(youtube);
        dest.writeString(source);
        dest.writeTypedList(ingredients);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };
}
