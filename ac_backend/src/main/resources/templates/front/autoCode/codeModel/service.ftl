package ${map.packPath!''};

import com.nnte.framework.base.BaseService;
import org.springframework.stereotype.Component;

@Component
public class ${map.classPri!''}Service extends BaseService<${map.classPri!''}Dao,${map.classPri!''}> {
    public ${map.classPri!''}Service(){
        super(${map.classPri!''}Dao.class);
    }
}
