package cli;

import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandLineParser {
    private CommandLineManagerImpl manager = new CommandLineManagerImpl();

    //Создаем команды, которые будут вводиться в консоль пользователем
    //В общем, вряд ли это понадобится. Сперва сделала с помощью этой либы, но в итоге столкнулась с тем, что
    //в этом варианте невозможен интерактивный режим, который нам нужен. Скорее всего, это можно удалить)
    @Deprecated
    private Options createOptions() {
        Option feedOption = new Option("r", "rss", true, "Add new rss feed");
        feedOption.setArgs(1);

        Option feedParamOption = new Option("fp", "feedparam", true, "Choose params for feed");
        feedParamOption.setArgs(Option.UNLIMITED_VALUES);
        feedParamOption.setValueSeparator(',');

        Option itemParamOption = new Option("ip", "itemparam", true, "Choose params for item");
        itemParamOption.setArgs(Option.UNLIMITED_VALUES);
        itemParamOption.setValueSeparator(',');

        Option fileOption = new Option("f", "file", true, "Create new file");
        fileOption.setArgs(1);

        Option helpOption = new Option("h", "help", false, "Help");

        Options options = new Options();
        options.addOption(feedOption);
        options.addOption(feedParamOption);
        options.addOption(itemParamOption);
        options.addOption(fileOption);
        options.addOption(helpOption);

        return options;
    }

    //Здесь, собственно, происходит парсинг нашей строки. Куча условий на все команды
    //Используется в паре с предыдущим
    @Deprecated
    public void parse(String[] commandLineArguments) {
        final Options options = createOptions();

        org.apache.commons.cli.CommandLineParser commandLineParser = new DefaultParser();
        try {
            CommandLine commandLine = commandLineParser.parse(options, commandLineArguments);
            manager.init();

            if (commandLine.hasOption("ip")) {
                String[] params = commandLine.getOptionValues("ip");
                if (params.length > 0) {
                    manager.setItemParams(Arrays.asList(params));
                } else {
                    manager.getItemParams();
                }
            }
            if (commandLine.hasOption("fp")) {
                String[] params = commandLine.getOptionValues("fp");
                if (params.length > 0) {
                    manager.setFeedParams(Arrays.asList(params));
                } else {
                    manager.getFeedParams();
                }
            }
            if (commandLine.hasOption("r")) {
                if (commandLine.hasOption("f")) {
                    String rssLink = commandLine.getOptionValue("r");
                    String file = commandLine.getOptionValue("f");
                    manager.createFileIfNotExists(file);
                    manager.addRssToFile(rssLink, file);
                } else {
                    //Нужно решить, что делать в этом случае: объявлять пользователю, что файл обязателен
                    //или же пихать rss еще куда
                }
            } else if (commandLine.hasOption("f")) {
                String file = commandLine.getOptionValue("f");
                manager.createFileIfNotExists(file);
            } else if (commandLine.hasOption("h")) {
                manager.printHelp(options);
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void parse(String inputString) {
        manager.init();

        //Создание из входной строки мапы <команда, список аргументов>
        String[] blocks = inputString.split("-");
        Map<String, List<String>> commandsMap = Arrays.stream(blocks)
                .map(str -> Arrays.asList(str.split(" ")))
                .collect(Collectors.toMap(
                        e -> e.get(0),
                        e -> e.size() > 1 ? e.subList(1, e.size()) : Collections.EMPTY_LIST
                ));

        //А вот дальше нужно все хорошо обдумать. Какие команды в каких сочетаниях могут быть
        //Плюс тут все же сейчас довольно печальная обработка аргументов
        if (commandsMap.containsKey("ip")) {
            List params = commandsMap.get("ip");
            if (params.size() > 0) {
                manager.setItemParams(params);
            } else {
                manager.getItemParams();
            }
        }
        if (commandsMap.containsKey("fp")) {
            List params = commandsMap.get("fp");
            if (params.size() > 0) {
                manager.setFeedParams(params);
            } else {
                manager.getFeedParams();
            }
        }
        if (commandsMap.containsKey("r")) {
            if (commandsMap.containsKey("f")) {
                String rssLink = commandsMap.get("r").get(0);
                String file = commandsMap.get("f").get(0);
                manager.createFileIfNotExists(file);
                manager.addRssToFile(rssLink, file);
            } else {
                //Нужно решить, что делать в этом случае: объявлять пользователю, что файл обязателен
                //или же пихать rss еще куда
            }
        } else if (commandsMap.containsKey("f")) {
            String file = commandsMap.get("f").get(0);
            manager.createFileIfNotExists(file);
        } else if (commandsMap.containsKey("h")) {
            //В этом варианте описание команд придется хардкодить
        }

    }
}
