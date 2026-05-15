# GWH_TJ_PLT_H
#Entity #Standard #INVENTORY

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | PLTH_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | PLTH_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | PLTH_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | PLTH_NUM | NVARCHAR2(26) | N | Pallet Number / パレットNo. |
| 22 | PLTH_AS_KND | NVARCHAR2(4) | N | Arrival and Shipping Kind/入出荷区分 |
| 23 | PLTH_PRT_YMD | DATE | N | Printed Date/印刷日 |
| 24 | PLTH_PLT_YMD | DATE | Y | Palletaize Date/パレタイズ日 |
| 25 | PLTH_OP_YMD | DATE | Y | Operation Date/作業日 |
| 26 | PLTH_AREA_COD | CHAR(3) | Y | Area/エリア |
| 27 | PLTH_RACK_COD | NVARCHAR2(20) | Y | Rack/ラック |
| 28 | PLTH_PSTN_COD | NVARCHAR2(6) | Y | Position/ポジション |
| 29 | PLTH_LVL_COD | NVARCHAR2(4) | Y | Level/レベル |
| 30 | PLTH_RWGT | NUMBER(12,6) | Y | Weight(Result.) / パレット重量 |
| 31 | PLTH_RM3 | NUMBER(12,6) | Y | Volume(Result.) / パレット容積 |
| 32 | PLTH_OP_FLG | CHAR(1) | N | Operation Flag/作業フラグ(入荷：棚付) |
| 33 | PLTH_CNFM_FLG | CHAR(1) | N | Confirmed Flag/確定フラグ |
| 34 | PLTH_RMKS | NVARCHAR2(1000) | Y | Remarks / 備考 |
| 35 | PLTH_COI1 | NVARCHAR2(200) | Y | Customized Only Item 01 / カスタマイズ項目01 |
| 36 | PLTH_COI2 | NVARCHAR2(200) | Y | Customized Only Item 02 / カスタマイズ項目02 |
| 37 | PLTH_COI3 | NVARCHAR2(200) | Y | Customized Only Item 03 / カスタマイズ項目03 |
| 38 | PLTH_COI4 | NVARCHAR2(200) | Y | Customized Only Item 04 / カスタマイズ項目04 |
| 39 | PLTH_COI5 | NVARCHAR2(200) | Y | Customized Only Item 05 / カスタマイズ項目05 |
| 40 | PLTH_COI6 | NVARCHAR2(200) | Y | Customized Only Item 06 / カスタマイズ項目06 |
| 41 | PLTH_COI7 | NVARCHAR2(200) | Y | Customized Only Item 07 / カスタマイズ項目07 |
| 42 | PLTH_COI8 | NVARCHAR2(200) | Y | Customized Only Item 08 / カスタマイズ項目08 |
| 43 | PLTH_COI9 | NVARCHAR2(200) | Y | Customized Only Item 09 / カスタマイズ項目09 |
| 44 | PLTH_COI10 | NVARCHAR2(200) | Y | Customized Only Item 10 / カスタマイズ項目10 |
| 45 | PLTH_COI11 | NVARCHAR2(200) | Y | Customized Only Item 11 / カスタマイズ項目11 |
| 46 | PLTH_COI12 | NVARCHAR2(200) | Y | Customized Only Item 12 / カスタマイズ項目12 |
| 47 | PLTH_COI13 | NVARCHAR2(200) | Y | Customized Only Item 13 / カスタマイズ項目13 |
| 48 | PLTH_COI14 | NVARCHAR2(200) | Y | Customized Only Item 14 / カスタマイズ項目14 |
| 49 | PLTH_COI15 | NVARCHAR2(200) | Y | Customized Only Item 15 / カスタマイズ項目15 |
| 50 | PLTH_COI16 | NVARCHAR2(200) | Y | Customized Only Item 16 / カスタマイズ項目16 |
| 51 | PLTH_COI17 | NVARCHAR2(200) | Y | Customized Only Item 17 / カスタマイズ項目17 |
| 52 | PLTH_COI18 | NVARCHAR2(200) | Y | Customized Only Item 18 / カスタマイズ項目18 |
| 53 | PLTH_COI19 | NVARCHAR2(200) | Y | Customized Only Item 19 / カスタマイズ項目19 |
| 54 | PLTH_COI20 | NVARCHAR2(200) | Y | Customized Only Item 20 / カスタマイズ項目20 |
| 55 | PLTH_COI21 | NVARCHAR2(200) | Y | Customized Only Item 21 / カスタマイズ項目21 |
| 56 | PLTH_COI22 | NVARCHAR2(200) | Y | Customized Only Item 22 / カスタマイズ項目22 |
| 57 | PLTH_COI23 | NVARCHAR2(200) | Y | Customized Only Item 23 / カスタマイズ項目23 |
| 58 | PLTH_COI24 | NVARCHAR2(200) | Y | Customized Only Item 24 / カスタマイズ項目24 |
| 59 | PLTH_COI25 | NVARCHAR2(200) | Y | Customized Only Item 25 / カスタマイズ項目25 |
| 60 | PLTH_COI26 | NVARCHAR2(200) | Y | Customized Only Item 26 / カスタマイズ項目26 |
| 61 | PLTH_COI27 | NVARCHAR2(200) | Y | Customized Only Item 27 / カスタマイズ項目27 |
| 62 | PLTH_COI28 | NVARCHAR2(200) | Y | Customized Only Item 28 / カスタマイズ項目28 |
| 63 | PLTH_COI29 | NVARCHAR2(200) | Y | Customized Only Item 29 / カスタマイズ項目29 |
| 64 | PLTH_COI30 | NVARCHAR2(200) | Y | Customized Only Item 30 / カスタマイズ項目30 |
| 65 | PLTH_COI31 | NVARCHAR2(200) | Y | Customized Only Item 31 / カスタマイズ項目31 |
| 66 | PLTH_COI32 | NVARCHAR2(200) | Y | Customized Only Item 32 / カスタマイズ項目32 |
| 67 | PLTH_COI33 | NVARCHAR2(200) | Y | Customized Only Item 33 / カスタマイズ項目33 |
| 68 | PLTH_COI34 | NVARCHAR2(200) | Y | Customized Only Item 34 / カスタマイズ項目34 |
| 69 | PLTH_COI35 | NVARCHAR2(200) | Y | Customized Only Item 35 / カスタマイズ項目35 |
| 70 | PLTH_COI36 | NVARCHAR2(200) | Y | Customized Only Item 36 / カスタマイズ項目36 |
| 71 | PLTH_COI37 | NVARCHAR2(200) | Y | Customized Only Item 37 / カスタマイズ項目37 |
| 72 | PLTH_COI38 | NVARCHAR2(200) | Y | Customized Only Item 38 / カスタマイズ項目38 |
| 73 | PLTH_COI39 | NVARCHAR2(200) | Y | Customized Only Item 39 / カスタマイズ項目39 |
| 74 | PLTH_COI40 | NVARCHAR2(200) | Y | Customized Only Item 40 / カスタマイズ項目40 |
| 75 | PLTH_COIL1 | NVARCHAR2(510) | Y | Customized Only Item (Long) 01 / カスタマイズ項目(long)01 |
| 76 | PLTH_COIL2 | NVARCHAR2(510) | Y | Customized Only Item (Long) 02 / カスタマイズ項目(long)02 |
| 77 | PLTH_COIL3 | NVARCHAR2(510) | Y | Customized Only Item (Long) 03 / カスタマイズ項目(long)03 |
| 78 | PLTH_COIL4 | NVARCHAR2(510) | Y | Customized Only Item (Long) 04 / カスタマイズ項目(long)04 |
| 79 | PLTH_COIL5 | NVARCHAR2(510) | Y | Customized Only Item (Long) 05 / カスタマイズ項目(long)05 |
| 80 | PLTH_COIL6 | NVARCHAR2(510) | Y | Customized Only Item (Long) 06 / カスタマイズ項目(long)06 |
| 81 | PLTH_COIL7 | NVARCHAR2(510) | Y | Customized Only Item (Long) 07 / カスタマイズ項目(long)07 |
| 82 | PLTH_COIL8 | NVARCHAR2(510) | Y | Customized Only Item (Long) 08 / カスタマイズ項目(long)08 |
| 83 | PLTH_COIL9 | NVARCHAR2(510) | Y | Customized Only Item (Long) 09 / カスタマイズ項目(long)09 |
| 84 | PLTH_COIL10 | NVARCHAR2(510) | Y | Customized Only Item (Long) 10 / カスタマイズ項目(long)10 |
| 85 | PLTH_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 86 | PLTH_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 87 | PLTH_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 88 | PLTH_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 89 | PLTH_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 90 | PLTH_CNI6_NUM | NUMBER(9) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 91 | PLTH_CNI7_NUM | NUMBER(9) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 92 | PLTH_CNI8_NUM | NUMBER(9) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 93 | PLTH_CNI9_NUM | NUMBER(9) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 94 | PLTH_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 / 顧客専用数字項目10 |
| 95 | PLTH_CNI11_NUM | NUMBER(14,4) | Y | Customized Number Item 11 / 顧客専用数字項目11 |
| 96 | PLTH_CNI12_NUM | NUMBER(14,4) | Y | Customized Number Item 12 / 顧客専用数字項目12 |
| 97 | PLTH_CNI13_NUM | NUMBER(14,4) | Y | Customized Number Item 13 / 顧客専用数字項目13 |
| 98 | PLTH_CNI14_NUM | NUMBER(14,4) | Y | Customized Number Item 14 / 顧客専用数字項目14 |
| 99 | PLTH_CNI15_NUM | NUMBER(14,4) | Y | Customized Number Item 15 / 顧客専用数字項目15 |
| 100 | PLTH_MSRL_NUM | NVARCHAR2(56) | Y | Palletize Master Serial No / パレタイズマスターシリアルNo |
