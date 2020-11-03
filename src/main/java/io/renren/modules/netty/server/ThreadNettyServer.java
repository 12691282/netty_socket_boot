package io.renren.modules.netty.server;

import io.renren.common.utils.BeanUtils;

/***
 * 线程级netty父类
 */
public class ThreadNettyServer {

    protected void isReadyToGo(){

        /**
         * 等待spring启动完成后开启netty线程池
         */
        while (BeanUtils.isApplicationContextReady()){
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
