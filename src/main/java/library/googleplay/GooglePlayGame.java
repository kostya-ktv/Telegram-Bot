package library.googleplay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder(builderClassName = "GooglePlayGameBuilder", toBuilder = true)
@JsonDeserialize(builder = GooglePlayGame.GooglePlayGameBuilder.class)
@Getter
@EqualsAndHashCode(exclude = { "genre", "devEmail"})

public final class GooglePlayGame  {

    @JsonProperty("Название игры") private final String title;

    @JsonProperty("Категория") private final String genre;

    @JsonProperty("Стоимость игры") private final String price;

    @JsonProperty("Дата последнего обновления") private final String lastUpdated;

    @JsonProperty("Размер установочного файла") private final String size;

    @JsonProperty("Текущая версия") private final String currentVersion;

    @JsonProperty("Требуемая версия Android") private final String requiresAndroid;

    @JsonProperty("Внутриигровые покупки") private final String iap;

    @JsonProperty("Связаться с разработчиком") private final String devEmail;

    @JsonProperty("Изображение игры") private final String pictureURL;

    @Override
    public String toString() {
        return
                "Название игры: " + title +
                "\nДата последнего обновления: " + lastUpdated +
                "\nТекущая версия: " + currentVersion +
                "\nТребуемая версия Android: " + requiresAndroid +
                "\nРазмер установочного файла: " + size +
                "\nКатегория: " + genre +
                "\nСтоимость игры: " + price +
                "\nВнутриигровые покупки: " + iap +
                "\nСвязаться с разработчиком: " + devEmail;
    }
}
