package io.renren.client;


import org.junit.Test;


//@RunWith(SpringRunner.class)
//@SpringBootTest
public class EvnSocketIOClientTest {


    private static String[] env_messageArr = {
            "{\"gasStationNo\":\"A01\",\"hmac\":\"bd98ce0f63265ba0f815e92bedb607e59914b076a09a73dab1a161508e0b54db\"," +
            "\"message\":{\"messageId\": \"123dw32\",\"busObject\":{\"fxyl\":1,\"gybjsj\":2,\"gyyjsj\":3,\"gyyjsx\":1," +
            "\"gyyjxx\":4,\"hjsjcy\":2,\"monitorTime\":\"2020-12-11 10:11:21\",\"qybybjzhsj\":2,\"qybyjsxz\":12.1,\"yjcsbl\":52.1}}," +
            "\"time\":\"2020-09-25 16:47\",\"version\":\"1\",\"event\":\"GSConfigurationDataEvent\"}",

            "{\"gasStationNo\":\"A02\",\"hmac\":\"e92bedb607e59914bd98c73dab1a1e0f63265ba0508e0b54dbf815076a09a61\"," +
            "\"message\":{\"messageId\": \"23swew2\",\"busObject\":[{\"monitorTime\": \"2017-05-25 15:15:22\",\"jyjId\":\"V2\",\"jyqId\": \"w1\",\"al\": 1.23, \"qll\":32.23,\"yll\": 21.32}]}," +
            "\"time\":\"2020-09-25 16:47\",\"version\":\"1\",\"event\":\"GSRecycleDataEvent\"}",

            "{\"gasStationNo\":\"A03\",\"hmac\":\"e92bedb607e59914bd98c73dab1a1e0f63265ba0508e0b54dbf815076a09a61\"," +
            "\"message\":{\"messageId\": \"23swew2\",\"busObject\":[{\"monitorTime\":\"2018-02-15 15:15:22\",\"ygyl\": 12,\"yzyl\": 12.32,\"xnd\": 12.23,\"ywd\": 13.22}]}," +
            "\"time\":\"2020-09-25 16:47\",\"version\":\"1\",\"event\":\"GSEnvironmentDataEvent\"}",

            "{\"gasStationNo\":\"A04\",\"hmac\":\"e92bedb607e59914bd98c73dab1a1e0f63265ba0508e0b54dbf815076a09a61\"," +
            "\"message\":{\"messageId\": \"322\",\"busObject\":{\"monitorTime\": \"2020-09-28 15:15:22\",\"al\": \"1:2;2:1;3:2;4:0;5:0;6:0;7:0;8:0;\"," +
            "\"mb\": \"0\",\"yz\": \"1\",\"ygyl\": \"1\",\"clzznd\": \"1\",\"pv\": \"0\",\"clzzqd\": \"0\",\"clzztz\": \"0\",\"xyhqg\": \"0\"}}," +
            "\"time\":\"2020-09-25 16:47\",\"version\":\"1\",\"event\":\"GSAlarmDataEvent\"}"
    };



    public static void main(String[] args) throws InterruptedException {
//        int j = 0;
//        while (true){
//            j++;

            for (int i = 0; i <env_messageArr.length; i++) {
//                new NettyClient(env_messageArr[i]).sendData();
                  new Thread(new NettyClient(env_messageArr[i])).start();
//                Thread.currentThread().sleep(10000);
                  Thread.currentThread().sleep(5000);
            }

//            if(j>10){
//
//                break;
//            }

//        }


    }
//      new Thread(new NettyClient(env_messageArr[i])).start();
    @Test
    public void test() throws Exception {

        int i = 1;
        while (true){
            for (int j = 0; j<env_messageArr.length; j++)
            new Thread(new NettyClient(env_messageArr[0])).start();
            Thread.currentThread().sleep(10000);
            i++;
            if(i >= 3){
                break;
            }
        }

    }


}
