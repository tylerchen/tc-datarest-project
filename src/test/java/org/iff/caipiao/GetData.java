/*******************************************************************************
 * Copyright (c) 2019-02-25 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.caipiao;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.FCS;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.RequestHelper;

import java.io.File;
import java.util.HashMap;

/**
 * GetData
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-02-25
 * auto generate by qdp.
 */
public class GetData {
    public static final String beginIssue = "2003001";
    public static final String endIssue = "2019027";

    public static void main(String[] args) throws Exception {
        //http://kaijiang.zhcw.com/lishishuju/jsp/ssqInfoList.jsp?czId=1&beginIssue=2003001&endIssue=2019021&currentPageNum=1
        String pageNo = "1";
        String url = "http://kaijiang.zhcw.com/lishishuju/jsp/ssqInfoList.jsp";
        String header = "序号|开奖日期|期号|中奖号码|销售额(元)|一等奖注数|一等奖奖金|二等奖注数|二等奖奖金|三等奖注数|三等奖奖金|奖池(元)";
        StringBuilder sb = new StringBuilder(1024);
        sb.append(header).append('\n');
        for (int pageCount = 1; pageCount <= 7; pageCount++) {
            pageNo = pageCount + "";
            RequestHelper.RequestResult result = RequestHelper.get(url, MapHelper.toMap("czId", "1", "beginIssue", beginIssue, "endIssue", endIssue, "currentPageNum", pageNo), new HashMap<>());
            String html = result.getBody();
            //System.out.println(html);
            String content = StringUtils.substringAfter(html, "<div class=\"chaJie\">");
            content = StringUtils.substringBefore(content, "</div>");
            //System.out.println(content);
            String body = StringUtils.substringAfter(content, "<tbody>");
            body = StringUtils.substringBefore(body, "</tbody>");
            //System.out.println(body);
            String[] dataRows = StringUtils.splitByWholeSeparator(StringUtils.replaceEach(body, new String[]{"<tr>", "<td class=\"kaiHao\">", ",", "-"}, new String[]{"", "", "", ""}), "</tr>");
            if (dataRows == null) {
                continue;
            }
            for (String dataRow : dataRows) {
                dataRow = dataRow.trim();
                if (dataRow.length() < 1) {
                    continue;
                }
                String[] datas = StringUtils.splitByWholeSeparator(StringUtils.replaceEach(dataRow, new String[]{"<td>", "<span>", "</span>"}, new String[]{"", "", ""}).trim(), "</td>");
                if (datas.length < 10) {
                    continue;
                }
                for (int i = 0; i < datas.length; i++) {
                    datas[i] = StringUtils.trim(datas[i]);
                }
                String join = StringUtils.join(datas, "|").trim();
                if (join.endsWith("|")) {
                    join = join.substring(0, join.length() - 1);
                }
                sb.append(join).append('\n');
            }
        }
        System.out.println(sb);
        FileUtils.write(new File(FCS.get("/Users/zhaochen/dev/caipiao/caipiao-{begin}-{end}.txt", beginIssue, endIssue).toString()), sb, "UTF-8");
    }
}
