

import java.math.BigInteger;
import java.util.Random;

public class RSA {
	private final int PRIME_SIZE = 512;
	
	// Use BigInt so there is no chance of Over flow
	private BigInteger p;
	private BigInteger q;
	private BigInteger n;
	private BigInteger phi;
	private BigInteger e;
	private BigInteger d;
	
	public RSA(){
		System.out.println("RSA encryption algorithm");
	}
	
	/**
	 * Creates public and private keys for RSA
	 *  step 1: select 2 large random prime numbers p and q
	 *  step 2: calculate n = p * q.
	 *  step 3: calculate ø(n) = (p-1) * (q-1)
	 *  step 4: find e such that gcd(e,ø(n)) = 1 ; 1 < e < ø(n)
	 *  step 5: Calculate d such that e.d = 1 (mod ø(n))
	 */
	public void createKeys(){
		//step 1
		p = BigInteger.probablePrime(PRIME_SIZE, new Random());
		q = BigInteger.probablePrime(PRIME_SIZE,new Random());
		
		//step 2
		n = calcN(p,q);
		
		//step 3
		phi = calcPhiN(p,q);
		
		//step 4
		e = calcE(phi,e);
		
		//step 5
		d = calcD(e,phi);
		
		System.out.println("Public key("+n.intValue() +", " + e.intValue()+")");
		System.out.println("Private key(" + d.intValue() +", "+ n.intValue() +")");
	}
	/**
	 * encryption returns a ciphertext
	 *  m^e (mod n)
	 * @param pt
	 * @return ciphertext
	 */
	public BigInteger encrypt(BigInteger pt, BigInteger publice, BigInteger publicn){
		BigInteger encrypt = pt.modPow(publice, publicn);
		return encrypt;
	}
	/**
	 *  Decryption needs the ciphertext
	 *    c^d (mod n)
	 * @param ct
	 * @return plaintext
	 */
	public BigInteger decrypt(BigInteger ct,BigInteger privated, BigInteger publicn){
		BigInteger decrypt = ct.modPow(privated, publicn);
		return decrypt;
	}
	
	/**
	 * calculate n = p * q.
	 * @param p
	 * @param q
	 * @return p * q
	 */
	private BigInteger calcN(BigInteger p, BigInteger q){
		return p.multiply(q);
	}
	
	/**
	 * calculate ø(n) = (p-1) * (q-1)
	 * @param p
	 * @param q
	 * @return ø(n)
	 */
	private BigInteger calcPhiN(BigInteger p, BigInteger q){
		BigInteger temp1 = p.subtract(BigInteger.valueOf(1));
		BigInteger temp2 = q.subtract(BigInteger.valueOf(1));
		
		return temp1.multiply(temp2);
	}
	/**
	 * we need to find e...
	 * Loop creating random numbers until the gcd =1
	 * e such that gcd(e,ø(n)) = 1 ; 1 < e < ø(n)
	 * @param p this is phi
	 * @param eTemp
	 * @return the value of e
	 */
	private BigInteger calcE(BigInteger p,BigInteger eTemp){
		do{
			eTemp = new BigInteger(2 * PRIME_SIZE,new Random());
			BigInteger sa = eTemp.gcd(p);
			System.out.println(sa.intValue());
			}while(eTemp.gcd(p).intValue() != 1); //loop until gcd() = 1 
		return eTemp;
	}
	
	/**
	 * Calculate d such that e.d = 1 (mod ø(n))
	 * We need e*d (mod ø(n) to be congruent to 1 mod (mod ø(n)
	 * @param e
	 * @param p
	 * @return d
	 */
	private BigInteger calcD(BigInteger e,BigInteger p){
		return e.modInverse(p);
	}
}
