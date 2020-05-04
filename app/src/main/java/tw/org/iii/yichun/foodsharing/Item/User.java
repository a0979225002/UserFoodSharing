package tw.org.iii.yichun.foodsharing.Item;

/**
 * 一開始登入時拿取 該名user的所有資訊
 */
public class User {
    private static String id;
    private static String name;
    private static String account;
    private static String phone;
    private static String createtime;
    private static String modified;
    private static String email;
    private static String userimg;
    private static String address;
    private static String city;
    private static String dist;
    private static Double Latitude;//user現在位置,經度
    private static Double Longitude;//user現在位置,緯度

    public static Double getLatitude() {
        return Latitude;
    }

    public static void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public static Double getLongitude() {
        return Longitude;
    }

    public static void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        User.city = city;
    }

    public static String getDist() {
        return dist;
    }

    public static void setDist(String dist) {
        User.dist = dist;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        User.address = address;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        User.id = id;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        User.name = name;
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        User.account = account;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        User.phone = phone;
    }

    public static String getCreatetime() {
        return createtime;
    }

    public static void setCreatetime(String createtime) {
        User.createtime = createtime;
    }

    public static String getModified() {
        return modified;
    }

    public static void setModified(String modified) {
        User.modified = modified;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static String getUserimg() {
        return userimg;
    }

    public static void setUserimg(String userimg) {
        User.userimg = userimg;
    }
}
