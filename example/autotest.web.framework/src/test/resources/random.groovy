class SuRenRandom
{
	String randomPhoneNum(){
		def preNum = [
			182, 152
		]
		
		def num = (preNum[0] + '')
		8.times{num = num.concat('' + (int)(Math.random() * 10))}
		
		return num
	}
	
	String randomZipCode(){
		def zip = ''
		6.times{zip = zip.concat('' + (int)(Math.random() * 10))}
		return zip
	}
	
	String randomEmail(){
		def email = ''
		4.times{char a = 'a'.charAt(0) + (int)(Math.random()*10); email = email + a;}
		email += '@126.com'
		return email
	}
}
