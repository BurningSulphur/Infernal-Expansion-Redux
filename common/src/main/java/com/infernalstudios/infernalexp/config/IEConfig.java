package com.infernalstudios.infernalexp.config;

public class IEConfig {
    public Client client = new Client();
    public Common common = new Common();

    public static class Client {
    }

    public static class Common {
        public int geyserSteamHeight = 8;

        public boolean volineTurnIntoGeyser = true;
        public boolean volineSleepWhenFed = true;
        public boolean volineGetBig = true;
    }
}