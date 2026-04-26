package at.spengergasse.dp_backend.service;

import at.spengergasse.dp_backend.GlobalConstat;
import org.springframework.stereotype.Service;

@Service
public class BaseService
{
    protected GlobalConstat globalConstat = new GlobalConstat();
}//end Class
