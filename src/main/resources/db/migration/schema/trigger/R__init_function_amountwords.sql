CREATE OR REPLACE FUNCTION spellNumericValue( pValue numeric, currency varchar)
  RETURNS text AS
$BODY$
DECLARE
  _dollar bigint = trunc(pValue)::text;
  _cents int = ((pValue - trunc(pValue))*100)::int;
  _spelledAmount text = '' ;
  _brokenOut int[] ;
  _pos integer = 0;
  _word text ;
  _tempVal int = 0 ;
BEGIN

	--break the number down into separate elements each containing a max of 3
        --digits.  Number 23321456 is broken in array like so {456,321,23}
	WHILE _dollar > 0 loop
		_brokenOut = array_append(_brokenOut, (_dollar%1000)::int);
		_dollar = trunc(_dollar/1000);
		_pos = _pos + 1;
	End Loop;

	--this works on numbers between 1 to 999 transforming into english words, then goes to the
	--next set of numbers in the array working backwards as the array was loaded backwards
	--Meaning the highest value is the last element of the array _brokenOut
	--This assumes words thousands millions, billions... occurs every 10^3
	while _pos > 0 loop
		_tempVal = _brokenOut[_pos] ;  --use _tempVal to work on using the array directly has big performance hit.
		if _tempVal >99 then
			IF    _tempVal > 899 THEN _spelledAmount = _spelledAmount || 'Nine Hundred ' ;
			elsif _tempVal > 799 THEN _spelledAmount = _spelledAmount || 'Eight Hundred ' ;
			elsif _tempVal > 699 THEN _spelledAmount = _spelledAmount || 'Seven Hundred ' ;
			elsif _tempVal > 599 THEN _spelledAmount = _spelledAmount || 'Six Hundred ' ;
			elsif _tempVal > 499 THEN _spelledAmount = _spelledAmount || 'Five Hundred ' ;
			elsif _tempVal > 399 THEN _spelledAmount = _spelledAmount || 'Four Hundred ' ;
			elsif _tempVal > 299 THEN _spelledAmount = _spelledAmount || 'Three Hundred ' ;
			elsif _tempVal > 199 THEN _spelledAmount = _spelledAmount || 'Two Hundred ' ;
			elsif _tempVal > 99 THEN  _spelledAmount = _spelledAmount || 'One Hundred ' ;
			end if ;
		end if;

		if    _tempVal%100 = 10 THEN _spelledAmount = _spelledAmount || 'Ten ';
		elsif _tempVal%100 = 11 THEN _spelledAmount = _spelledAmount || 'Eleven ';
		elsif _tempVal%100 = 12 THEN _spelledAmount = _spelledAmount || 'Twelve ';
		elsif _tempVal%100 = 13 THEN _spelledAmount = _spelledAmount || 'Thirteen ';
		elsif _tempVal%100 = 14 THEN _spelledAmount = _spelledAmount || 'Fourteen ';
		elsif _tempVal%100 = 15 THEN _spelledAmount = _spelledAmount || 'Fifteen ';
		elsif _tempVal%100 = 16 THEN _spelledAmount = _spelledAmount || 'Sixteen ';
		elsif _tempVal%100 = 17 THEN _spelledAmount = _spelledAmount || 'Seventeen ';
		elsif _tempVal%100 = 18 THEN _spelledAmount = _spelledAmount || 'Eighteen ';
		elsif _tempVal%100 = 19 THEN _spelledAmount = _spelledAmount || 'Nineteen ';
		elsif _tempVal/10%10 =2 THEN _spelledAmount = _spelledAmount || 'Twenty ';
		elsif _tempVal/10%10 =3 THEN _spelledAmount = _spelledAmount || 'Thirty ' ;
		elsif _tempVal/10%10 =4 THEN _spelledAmount = _spelledAmount || 'Fourty ' ;
		elsif _tempVal/10%10 =5 THEN _spelledAmount = _spelledAmount || 'Fifty ' ;
		elsif _tempVal/10%10 =6 THEN _spelledAmount = _spelledAmount || 'Sixty ' ;
		elsif _tempVal/10%10 =7 THEN _spelledAmount = _spelledAmount || 'Seventy ' ;
		elsif _tempVal/10%10 =8 THEN _spelledAmount = _spelledAmount || 'Eighty ' ;
		elsif _tempVal/10%10 =9 THEN _spelledAmount = _spelledAmount || 'Ninety  ' ;
		End if ;


		if _tempVal%100 < 10 or _tempVal%100 > 20 then
			if    _tempVal%10 = 1 THEN _spelledAmount = _spelledAmount || 'One ';
			elsif _tempVal%10 = 2 THEN _spelledAmount = _spelledAmount || 'Two ';
			elsif _tempVal%10 = 3 THEN _spelledAmount = _spelledAmount || 'Three ';
			elsif _tempVal%10 = 4 THEN _spelledAmount = _spelledAmount || 'Four ';
			elsif _tempVal%10 = 5 THEN _spelledAmount = _spelledAmount || 'Five ';
			elsif _tempVal%10 = 6 THEN _spelledAmount = _spelledAmount || 'Six ';
			elsif _tempVal%10 = 7 THEN _spelledAmount = _spelledAmount || 'Seven ';
			elsif _tempVal%10 = 8 THEN _spelledAmount = _spelledAmount || 'Eight ';
			elsif _tempVal%10 = 9 THEN _spelledAmount = _spelledAmount || 'Nine ';
			end if ;
		end if ;

                --Based on array element tells us which word to use.
                --As the array is loaded backwards the highest value is
                --highest array element number. To take it higher values all
                --one needs to do is add more elsif statements.
		If _pos = 2 then
			_spelledAmount = _spelledAmount || 'Thousand ';
		elsif _pos = 3  then
			_spelledAmount = _spelledAmount || 'Million';
		elsif _pos = 4  then
			_spelledAmount = _spelledAmount || 'Billion ';
		elsif _pos = 5 then
			_spelledAmount = _spelledAmount || 'Trillion ';
		elsif _pos = 6 then
			_spelledAmount = _spelledAmount || 'Quadrillion ';
		elsif _pos = 7 then
			_spelledAmount = _spelledAmount || 'Quintillion ';
		else
			_spelledAmount = _spelledAmount || '';
		end if;

		_pos = _pos-1;
	end loop;

        --Functions primary purpose is to write out the amount on Checks
        --this can be dropped out if you don't need it.
        if pvalue <= 0.99 then
		_spelledAmount = _spelledAmount || 'Zero Dollars ';
	else
		_spelledAmount = _spelledAmount || 'Dollars ';
	end if ;

	if _cents = 0 then
		_spelledAmount = _spelledAmount || ' and Zero cents';
	else
		_spelledAmount = _spelledAmount || 'and ' || _cents::text || ' cents';
	end if ;
	return _SpelledAmount;

END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;