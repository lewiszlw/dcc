package lewiszlw.dcc.server.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Desc:
 *
 * @author zhanglinwei02
 * @date 2019-04-29
 */
@Data
@Accessors(chain = true)
public class AddConfigRequest {

    private String operator;

    private List<ConfigVO> configVOS;
}
