package cn.aposoft.tutorial.keygeneration.dfs;

import java.util.Properties;

import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PrimaryKeyIdHelper {

    Logger logger = LoggerFactory.getLogger(PrimaryKeyIdHelper.class);

    private Properties gatewayCfg;

    private long workerId = 0;

    private long datacenterId = 0;

    private static PrimaryKeyIdWorker idWorker = null;

    public PrimaryKeyIdHelper() {
    }

    public void init() {
        logger.info("################初始化 分布式主键策略########start########################");
        // 获取本机ip
        String local_ip = "";// IpUtil.getIp();
        logger.info("#####本机ip####{}", local_ip);
        // 获取主键策略配置
        String workerIdStr = gatewayCfg.getProperty(local_ip + ".workerId", null);
        String datacenterIdStr = gatewayCfg.getProperty(local_ip + ".datacenterId", null);
        logger.info("分布式事物主键datacenterId:{}", workerIdStr);
        logger.info("分布式事物主键workerId:{}", datacenterIdStr);
        // 检查机器id和数据中心id
        if (StringUtils.isEmpty(workerIdStr) || StringUtils.isEmpty(datacenterIdStr)) {
            logger.error("env.properties缺少【{}】的主键策略配置【datacenterId和workerId】", local_ip);
            logger.error("系统启动失败");
            System.exit(0);
        }
        this.workerId = Long.parseLong(workerIdStr);
        this.datacenterId = Long.parseLong(datacenterIdStr);
        if (idWorker == null) {
            idWorker = new PrimaryKeyIdWorker(this.workerId, this.datacenterId);
        }
        logger.info("################初始化 分布式主键策略#########end#######################");
    }

    public long generateId() {
        return idWorker.nextId();
    }

}
