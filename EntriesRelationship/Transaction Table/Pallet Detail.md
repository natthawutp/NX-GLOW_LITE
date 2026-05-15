# GWH_TJ_PLT_D
#Entity #Standard #INVENTORY

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag/論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC)/作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC)/作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id/作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id/作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id/作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone/作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC)/作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local)/作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC)/更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC)/更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID/更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID/更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID/更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone/更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC)/更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local)/更新タイムスタンプ（ローカル） |
| 18 | PLTD_CPNY_COD | NVARCHAR2(12) | N | Company Code/カンパニーコード |
| 19 | PLTD_WHS_COD | NVARCHAR2(8) | N | Warehouse Code/倉庫コード |
| 20 | PLTD_CUST_COD | NVARCHAR2(26) | N | Customer Code/顧客コード |
| 21 | PLTD_NUM | NVARCHAR2(26) | N | Pallet Number/パレットNo. |
| 22 | PLTD_AS_NUM | NVARCHAR2(12) | N | Arrival and Shipping Number/入出荷番号 |
| 23 | PLTD_ASLN_NUM | NUMBER(4) | N | Arrival and Shipping Line Number/入出荷ラインNo |
| 24 | PLTD_ASSQ_NUM | NUMBER(4) | N | Arrival and Shipping Seq Number/入出荷SEQNo |
| 25 | PLTD_COI1 | NVARCHAR2(200) | Y | Customized Only Item 01/カスタマイズ項目01 |
| 26 | PLTD_COI2 | NVARCHAR2(200) | Y | Customized Only Item 02/カスタマイズ項目02 |
| 27 | PLTD_COI3 | NVARCHAR2(200) | Y | Customized Only Item 03/カスタマイズ項目03 |
| 28 | PLTD_COI4 | NVARCHAR2(200) | Y | Customized Only Item 04/カスタマイズ項目04 |
| 29 | PLTD_COI5 | NVARCHAR2(200) | Y | Customized Only Item 05/カスタマイズ項目05 |
| 30 | PLTD_COI6 | NVARCHAR2(200) | Y | Customized Only Item 06/カスタマイズ項目06 |
| 31 | PLTD_COI7 | NVARCHAR2(200) | Y | Customized Only Item 07/カスタマイズ項目07 |
| 32 | PLTD_COI8 | NVARCHAR2(200) | Y | Customized Only Item 08/カスタマイズ項目08 |
| 33 | PLTD_COI9 | NVARCHAR2(200) | Y | Customized Only Item 09/カスタマイズ項目09 |
| 34 | PLTD_COI10 | NVARCHAR2(200) | Y | Customized Only Item 10/カスタマイズ項目10 |
| 35 | PLTD_COI11 | NVARCHAR2(200) | Y | Customized Only Item 11/カスタマイズ項目11 |
| 36 | PLTD_COI12 | NVARCHAR2(200) | Y | Customized Only Item 12/カスタマイズ項目12 |
| 37 | PLTD_COI13 | NVARCHAR2(200) | Y | Customized Only Item 13/カスタマイズ項目13 |
| 38 | PLTD_COI14 | NVARCHAR2(200) | Y | Customized Only Item 14/カスタマイズ項目14 |
| 39 | PLTD_COI15 | NVARCHAR2(200) | Y | Customized Only Item 15/カスタマイズ項目15 |
| 40 | PLTD_COI16 | NVARCHAR2(200) | Y | Customized Only Item 16/カスタマイズ項目16 |
| 41 | PLTD_COI17 | NVARCHAR2(200) | Y | Customized Only Item 17/カスタマイズ項目17 |
| 42 | PLTD_COI18 | NVARCHAR2(200) | Y | Customized Only Item 18/カスタマイズ項目18 |
| 43 | PLTD_COI19 | NVARCHAR2(200) | Y | Customized Only Item 19/カスタマイズ項目19 |
| 44 | PLTD_COI20 | NVARCHAR2(200) | Y | Customized Only Item 20/カスタマイズ項目20 |
| 45 | PLTD_COI21 | NVARCHAR2(200) | Y | Customized Only Item 21/カスタマイズ項目21 |
| 46 | PLTD_COI22 | NVARCHAR2(200) | Y | Customized Only Item 22/カスタマイズ項目22 |
| 47 | PLTD_COI23 | NVARCHAR2(200) | Y | Customized Only Item 23/カスタマイズ項目23 |
| 48 | PLTD_COI24 | NVARCHAR2(200) | Y | Customized Only Item 24/カスタマイズ項目24 |
| 49 | PLTD_COI25 | NVARCHAR2(200) | Y | Customized Only Item 25/カスタマイズ項目25 |
| 50 | PLTD_COI26 | NVARCHAR2(200) | Y | Customized Only Item 26/カスタマイズ項目26 |
| 51 | PLTD_COI27 | NVARCHAR2(200) | Y | Customized Only Item 27/カスタマイズ項目27 |
| 52 | PLTD_COI28 | NVARCHAR2(200) | Y | Customized Only Item 28/カスタマイズ項目28 |
| 53 | PLTD_COI29 | NVARCHAR2(200) | Y | Customized Only Item 29/カスタマイズ項目29 |
| 54 | PLTD_COI30 | NVARCHAR2(200) | Y | Customized Only Item 30/カスタマイズ項目30 |
| 55 | PLTD_COI31 | NVARCHAR2(200) | Y | Customized Only Item 31/カスタマイズ項目31 |
| 56 | PLTD_COI32 | NVARCHAR2(200) | Y | Customized Only Item 32/カスタマイズ項目32 |
| 57 | PLTD_COI33 | NVARCHAR2(200) | Y | Customized Only Item 33/カスタマイズ項目33 |
| 58 | PLTD_COI34 | NVARCHAR2(200) | Y | Customized Only Item 34/カスタマイズ項目34 |
| 59 | PLTD_COI35 | NVARCHAR2(200) | Y | Customized Only Item 35/カスタマイズ項目35 |
| 60 | PLTD_COI36 | NVARCHAR2(200) | Y | Customized Only Item 36/カスタマイズ項目36 |
| 61 | PLTD_COI37 | NVARCHAR2(200) | Y | Customized Only Item 37/カスタマイズ項目37 |
| 62 | PLTD_COI38 | NVARCHAR2(200) | Y | Customized Only Item 38/カスタマイズ項目38 |
| 63 | PLTD_COI39 | NVARCHAR2(200) | Y | Customized Only Item 39/カスタマイズ項目39 |
| 64 | PLTD_COI40 | NVARCHAR2(200) | Y | Customized Only Item 40/カスタマイズ項目40 |
| 65 | PLTD_COIL1 | NVARCHAR2(510) | Y | Customized Only Item(Long) 01/カスタマイズ項目(long)01 |
| 66 | PLTD_COIL2 | NVARCHAR2(510) | Y | Customized Only Item(Long) 02/カスタマイズ項目(long)02 |
| 67 | PLTD_COIL3 | NVARCHAR2(510) | Y | Customized Only Item(Long) 03/カスタマイズ項目(long)03 |
| 68 | PLTD_COIL4 | NVARCHAR2(510) | Y | Customized Only Item(Long) 04/カスタマイズ項目(long)04 |
| 69 | PLTD_COIL5 | NVARCHAR2(510) | Y | Customized Only Item(Long) 05/カスタマイズ項目(long)05 |
| 70 | PLTD_COIL6 | NVARCHAR2(510) | Y | Customized Only Item(Long) 06/カスタマイズ項目(long)06 |
| 71 | PLTD_COIL7 | NVARCHAR2(510) | Y | Customized Only Item(Long) 07/カスタマイズ項目(long)07 |
| 72 | PLTD_COIL8 | NVARCHAR2(510) | Y | Customized Only Item(Long) 08/カスタマイズ項目(long)08 |
| 73 | PLTD_COIL9 | NVARCHAR2(510) | Y | Customized Only Item(Long) 09/カスタマイズ項目(long)09 |
| 74 | PLTD_COIL10 | NVARCHAR2(510) | Y | Customized Only Item(Long) 10/カスタマイズ項目(long)10 |
| 75 | PLTD_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01/顧客専用数字項目01 |
| 76 | PLTD_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02/顧客専用数字項目02 |
| 77 | PLTD_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03/顧客専用数字項目03 |
| 78 | PLTD_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04/顧客専用数字項目04 |
| 79 | PLTD_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05/顧客専用数字項目05 |
| 80 | PLTD_CNI6_NUM | NUMBER(9) | Y | Customized Number Item 06/顧客専用数字項目06 |
| 81 | PLTD_CNI7_NUM | NUMBER(9) | Y | Customized Number Item 07/顧客専用数字項目07 |
| 82 | PLTD_CNI8_NUM | NUMBER(9) | Y | Customized Number Item 08/顧客専用数字項目08 |
| 83 | PLTD_CNI9_NUM | NUMBER(9) | Y | Customized Number Item 09/顧客専用数字項目09 |
| 84 | PLTD_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10/顧客専用数字項目10 |
| 85 | PLTD_CNI11_NUM | NUMBER(14,4) | Y | Customized Number Item 11/顧客専用数字項目11 |
| 86 | PLTD_CNI12_NUM | NUMBER(14,4) | Y | Customized Number Item 12/顧客専用数字項目12 |
| 87 | PLTD_CNI13_NUM | NUMBER(14,4) | Y | Customized Number Item 13/顧客専用数字項目13 |
| 88 | PLTD_CNI14_NUM | NUMBER(14,4) | Y | Customized Number Item 14/顧客専用数字項目14 |
| 89 | PLTD_CNI15_NUM | NUMBER(14,4) | Y | Customized Number Item 15/顧客専用数字項目15 |
