package com.renren.fenqi.auth.service.realm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.fenqi.auth.model.realm.Realm;
import com.renren.fenqi.auth.repository.realm.SystemRealmRepository;

@Service
public class RealmService {

	@Autowired
	private SystemRealmRepository sysRealmRepository;
	
	public List<Realm> getAllRealm(){
		Map<String, Object> andParam = new HashMap<String, Object>();
		andParam.put("status", 0);
		List<Realm> list = sysRealmRepository.list(Realm.class,andParam,null);
		return list;
	}
	public List<String> getAllRealmIds(){
		List<Realm> realmLst = getAllRealm();
		List<String> list =new ArrayList<>();
		for(Realm realm:realmLst){
			list.add(realm.getRealmId());
		}
		return list;
	}
}
