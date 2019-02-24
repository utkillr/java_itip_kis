package cli;

import org.apache.commons.cli.Options;

import java.util.List;

public interface CommandLineManager {
    /**
     * Начальная настройка менеджера
     */
    void init();

    /**
     * Вывод в консоль информации о доступных командах
     *
     * @param options команды
     */
    void printHelp(final Options options);

    /**
     * Создание файла, если он не существует
     *
     * @param file название файла
     */
    void createFileIfNotExists(final String file);

    /**
     * Вывод rss feed в указанный файл
     *
     * @param link ссылка на feed
     * @param file файл для записи
     */
    void addRssToFile(final String link, final String file);

    /**
     * Получить список используемых для item параметров
     */
    void getItemParams();

    /**
     * Задать список используемых для item параметров
     *
     * @param params выводимые параметры
     */
    void setItemParams(final List<String> params);

    /**
     * Получить список используемых для feed параметров
     */
    void getFeedParams();

    /**
     * Задать список используемых для feed параметров
     *
     * @param params выводимые параметры
     */
    void setFeedParams(final List<String> params);

}
