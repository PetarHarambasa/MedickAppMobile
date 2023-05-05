package hr.medick.properties;

public class UrlProperties {
    // ip of device that is vrtiti backend
    // žično ipadesa: 192.168.0.126 žično ne radi
    // bežično ipadrsa: 192.168.1.3 radi ko mašina
    // ipadrsa za run local emulator: 10.0.2.2
    // i guess dok publishamo backend na neki server stavljamo ip od servera
    // val url = "http://192.168.1.3:8080/mobileRegister" ili val url = "{ngrok link}/mobileRegister"
    public static final String IP_ADDRESS = "10.0.2.2";
}
