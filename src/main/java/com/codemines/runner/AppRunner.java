package com.codemines.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.codemines.entites.EligibilityDetails;
import com.codemines.repo.EligibiltyRepo;

@Component
public class AppRunner implements ApplicationRunner{

	//ye runner application k star hone time automatic ek baar chlegi 
	//abhi apan islie use kr rhe ki apan ko ek dumi data db mai daalna hai so apaan chahte hai ye data store ho jaaye
	
	@Autowired
	private EligibiltyRepo eligibiltyRepo;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		EligibilityDetails entity1=new EligibilityDetails();
		entity1.setName("john doe");
		entity1.setMobileNo(1231234565l);
		entity1.setGender('M');
		entity1.setSsn(112211221221l);
		entity1.setPlanName("GOLD");
		entity1.setPlanStatus("pending");
		eligibiltyRepo.save(entity1);
		
		EligibilityDetails entity2=new EligibilityDetails();
		entity2.setName("john smith");
		entity2.setMobileNo(1234444445l);
		entity2.setGender('F');
		entity2.setSsn(1124455555551221l);
		entity2.setPlanName("SILVER");
		entity2.setPlanStatus("approved");
		eligibiltyRepo.save(entity2);
	}

}//end
