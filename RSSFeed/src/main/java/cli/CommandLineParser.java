package cli;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Parser for CLI Wrapper
 */
public class CommandLineParser {
    private CommandLineManager manager = new CommandLineManager();

    /**
     * Parse input command (cli wrapper)
     *
     * @param inputString full command containing of command and its parameters
     * @return execution status:
     *      0 means no errors
     *      1 means graceful exit
     */
    public int parse(String inputString) {
        manager.init();

        Map<String, List<String>> commandMap = new HashMap<>();
        List<String> blocks = Arrays.asList(inputString.split(" "));
        commandMap.put(blocks.get(0), blocks.size() > 1 ? blocks.subList(1, blocks.size()) : Collections.EMPTY_LIST);

        // item properties
        if (commandMap.containsKey("ip")) {
            List<String> params = commandMap.get("ip");
            if (params.size() > 0) {
                // set item properties
                manager.setItemParams(params);
                manager.prettyPrint("Successfully set Item Properties to: " + params);
            } else {
                // get item properties
                manager.printItemParams();
            }
        }

        // channel properties
        if (commandMap.containsKey("cp")) {
            List<String> params = commandMap.get("fp");
            if (params.size() > 0) {
                // set channel properties
                manager.setChannelParams(params);
                manager.prettyPrint("Successfully set Channel Properties to: " + params);
            } else {
                // get channel properties
                manager.printChannelParams();
            }
        }

        // rss
        if (commandMap.containsKey("rss")) {
            List<String> params  = commandMap.get("rss");
            if (params.size() > 0) {
                switch (params.get(0)) {
                    case "add": {
                        if (params.size() != 3) {
                            throw new IllegalArgumentException("Not enough params for -rss add");
                        }
                        String rssLink = params.get(1);
                        String file = params.get(2);
                        manager.createFileIfNotExists(file);
                        manager.associateRssToFile(rssLink, file);
                        manager.prettyPrint(
                                "Successfully added new RSS Feed: " + rssLink,
                                "Associated file is: " + file
                        );
                        break;
                    }
                    case "del": {
                        if (params.size() != 2) {
                            throw new IllegalArgumentException("Not enough params for -rss del");
                        }
                        String rssLink = params.get(1);
                        manager.diassociateRss(rssLink);
                        manager.prettyPrint("Successfully removed new RSS Feed: " + rssLink);
                        break;
                    }
                    case "on": {
                        if (params.size() != 2) {
                            throw new IllegalArgumentException("Not enough params for -rss on");
                        }
                        String rssLink = params.get(1);
                        manager.turnRSSOn(rssLink);
                        manager.prettyPrint("Successfully turned RSS Feed " + rssLink + " on");
                        break;
                    }
                    case "off": {
                        if (params.size() != 2) {
                            throw new IllegalArgumentException("Not enough params for -rss off");
                        }
                        String rssLink = params.get(1);
                        manager.turnRSSOff(rssLink);
                        manager.prettyPrint("Successfully turned RSS Feed " + rssLink + " off");
                        break;
                    }
                }
            } else {
                manager.printRss();
            }
        } else if (commandMap.containsKey("time")) {
            List<String> params  = commandMap.get("time");
            if (params.size() > 0) {
                if (params.size() != 1) {
                    throw new IllegalArgumentException("Not enough params for -time");
                }
                String newTimeToPoll = params.get(0);
                manager.setTimeToPoll(Long.valueOf(newTimeToPoll));
                manager.prettyPrint("Successfully set polling time to " + newTimeToPoll);

            } else {
                manager.printTimeToPoll();
            }
        } else if (commandMap.containsKey("h")) {
            manager.printHelp();
        } else if (commandMap.containsKey("exit")) {
            return 1;
        }
        return 0;
    }
}
