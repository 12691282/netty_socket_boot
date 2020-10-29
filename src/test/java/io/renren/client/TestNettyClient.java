package io.renren.client;

public class TestNettyClient {

    private static String[] messageArr =
            {"P91_10054{\"msgID\":\"0241\",\"source\":\"0101\",\"time\":\"2020-09-14 14:54:11\"," +
                    "\"data\":[{\"1\":\"1\",\"2\":\"1\",\"3\":\"1\",\"4\":\"2\",\"5\":\"1\",\"6\":\"2020-09-14 10:54:11\"}," +
                    "{\"1\":\"3\",\"2\":\"4\",\"3\":\"5\",\"4\":\"3\",\"5\":\"3\",\"6\":\"2020-09-14 13:54:11\"}]}",

            "P91_10055{\"msgID\":\"0514\",\"source\":\"1214\",\"time\":\"2020-09-15 09:54:11\"," +
                    "\"data\":[{\"1\":\"液位仪品牌1\",\"2\":\"1\",\"3\":\"001\",\"4\":\"2020-09-15 01:54:11\"},{\"1\":\"液位仪品牌2\",\"2\":\"002\",\"3\":\"002\",\"4\":\"2020-09-15 02:24:01\"}]}",

            "P91_10056{\"msgID\":\"5214\",\"source\":\"5601\",\"time\":\"2020-05-21 09:54:11\"," +
                    "\"data\":[{\"1\":\"01\",\"2\":\"1221.22\",\"3\":\"0\",\"4\":\"2020-06-11 11:54:11\",\"5\":\"3.55\",\"6\":\"96.55\",\"7\":\"235.33\",\"8\":\"0\",\"9\":\"1\",\"10\":\"1\",\"11\":\"0\",\"12\":\"1\",\"13\":\"0\",\"14\":\"1\",\"15\":\"0\"}," +
                              "{\"1\":\"02\",\"2\":\"813.81\",\"3\":\"1\",\"4\":\"2020-06-21 01:14:41\",\"5\":\"82.17\",\"6\":\"61.21\",\"7\":\"719.08\",\"8\":\"1\",\"9\":\"0\",\"10\":\"0\",\"11\":\"1\",\"12\":\"0\",\"13\":\"1\",\"14\":\"0\",\"15\":\"1\"}]}",

            "P91_10057{\"msgID\":\"2311\",\"source\":\"3579\",\"time\":\"2020-06-11 19:54:11\"," +
                    "\"data\":[{\"1\":\"油枪号001\",\"2\":\"23.31\",\"3\":\"0\",\"4\":\"2020-06-10 09:54:11\",\"5\":\"28.84\",\"6\":\"19.62\"},{\"1\":\"油枪号002\",\"2\":\"544.23\",\"3\":\"1\",\"4\":\"2020-06-04 12:54:11\",\"5\":\"38.14\",\"6\":\"18.37\"}]}",

            "P91_10058{\"msgID\":\"521\",\"source\":\"8741\",\"time\":\"2020-07-20 13:49:01\"," +
                    "\"data\":[{\"1\":\"1\",\"2\":\"10003214\",\"3\":\"2\",\"4\":\"2020-07-23 01:14:11\"},{\"1\":\"2\",\"2\":\"100068224\",\"3\":\"4\",\"4\":\"2020-07-15 03:21:21\"}]}",

            //加油机连接状态信息
            "P91_10001{\"msgID\":\"123\",\"source\":\"951\",\"time\":\"2020-09-15 21:54:15\"," +
            "\"data\":[{\"1\":\"液位仪品牌1\",\"2\":\"1\",\"3\":\"2020-09-15 01:54:11\",\"4\":\"01\",\"5\":\"加油机品牌1\"},{\"1\":\"液位仪品牌2\",\"2\":\"2\",\"3\":\"2020-09-15 01:54:11\",\"4\":\"02\",\"5\":\"加油机品牌2\"}]}",

            //液位仪连接状态
            "P91_10005{\"msgID\":\"2734\",\"source\":\"415\",\"time\":\"2020-09-25 19:14:21\"," +
             "\"data\":[{\"1\":\"301201\",\"2\":\"1\",\"3\":\"2020-10-07 21:14:31\"},{\"1\":\"125402\",\"2\":\"0\",\"3\":\"2020-05-25 12:14:05\"}]}",

             //在线监测系统收配置数据
            "P91_10060{\"msgID\":\"3641\",\"source\":\"3244\",\"time\":\"2020-03-25 15:14:51\"," +
            "\"data\":[{\"1\":\"12.55\",\"2\":\"68.31\",\"3\":\"36125.61\",\"4\":\"2020-06-11 11:54:11\",\"5\":\"2020-12-04 11:54:11\",\"6\":\"96.55\",\"7\":\"235.33\",\"8\":\"2020-03-04 13:51:38\",\"9\":\"36.11\",\"10\":\"63.14\",\"11\":\"2020-03-04 13:51:38\",\"12\":\"2020-03-04 13:51:38\",\"13\":\"687.33\"}," +
                      "{\"1\":\"97.11\",\"2\":\"45.45\",\"3\":\"15543.74\",\"4\":\"2020-04-21 21:14:11\",\"5\":\"2020-02-14 15:49:21\",\"6\":\"62.40\",\"7\":\"367.85\",\"8\":\"2020-07-07 14:47:38\",\"9\":\"36.22\",\"10\":\"33.12\",\"11\":\"2020-01-03 13:51:38\",\"12\":\"2020-03-04 13:51:38\",\"13\":\"687.33\"}]}",

            //可燃气体浓度监测报警数据
            "P91_10061{\"msgID\":\"43123232\",\"source\":\"5597\",\"time\":\"2020-09-18 11:54:15\"," +
            "\"data\":[{\"1\":\"1221\",\"2\":\"1\",\"3\":\"2\",\"4\":\"31.21\",\"5\":\"1\",\"6\":\"2020-09-15 01:54:11\"},{\"1\":\"7451\",\"2\":\"2\",\"3\":\"21\",\"4\":\"6146.11\",\"5\":\"0\",\"6\":\"2020-09-18 21:34:55\"}]}"
            };


    public static void main(String[] args) throws InterruptedException {

//        new Thread(new NettyClient(messageArr[5])).start();

        int i = 1;
        while (true){
            for (int j = 0; j<messageArr.length; j++){
                Thread.sleep(15000);
                new Thread(new NettyClient(messageArr[j])).start();
                i++;
                if(i >= 3){
                    continue;
                }
            }
        }
    }
}
