package com.iot.nero.api_gateway.service.impl;
import com.iot.nero.api_gateway.common.ConfigUtil;
import com.iot.nero.api_gateway.core.core.ApiGatewayHandler;
import com.iot.nero.api_gateway.core.core.ApiMapping;
import com.iot.nero.api_gateway.core.firewall.IpCache;
import com.iot.nero.api_gateway.service.IIpTablesService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/5
 * Time   下午2:56
 */
public class IpTablesService implements IIpTablesService {
    Map<String, String> configMap;


    @ApiMapping("sys.ipTables.set")
    public boolean setIpTableStatus(String isOpen) throws IOException {
        configMap = ConfigUtil.configToMap();
        configMap.replace("ipTable.isOpen ", configMap.get("ipTable.isOpen "), isOpen);
        ApiGatewayHandler.setIpTableOpen(isOpen);
        return ConfigUtil.mapToConfig(configMap);
    }

    @ApiMapping("sys.ipTables.list")
    public List<String> getIP() throws IOException {
        IpCache ipCache = new IpCache();
        return new ArrayList<String>(ipCache.getIPSet());
    }

    @ApiMapping("sys.ipTables.add")
    public boolean addIP(String ip) throws IOException{
            IpCache ipCache = new IpCache();
            return ipCache.createBlankIP(ip.trim());
    }

    @ApiMapping("sys.ipTables.del")
    public boolean delIP(String ip) throws IOException {
            IpCache ipCache = new IpCache();
            return ipCache.deleteIP(ip.trim());
    }

    private boolean isIP(String addr)
    {
        if(addr.length() < 7 || addr.length() > 15 || "".equals(addr))
        {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        boolean ipAddress = mat.find();
        return ipAddress;
    }
}
