# GWH_TJ_EE_DAT
#Entity #Standard #EDI

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
| 18 | EE_DAT_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | EE_DAT_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | EE_DAT_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | EE_DAT_SR_NUM | NVARCHAR2(60) | N | Send/Receive Number / 送受信NO |
| 22 | EE_DAT_SRSQ_NUM | NUMBER(4) | N | Send/Receive Processing Seq / 処理シーケンス |
| 23 | EE_DAT_LINE_NUM | NUMBER(7) | N | File Line Number / ファイル行番号 |
| 24 | EE_DAT_RCV_ITM01 | NVARCHAR2(100) | Y | Receive Item 01 / 受信項目01 |
| 25 | EE_DAT_RCV_ITM02 | NVARCHAR2(100) | Y | Receive Item 02 / 受信項目02 |
| 26 | EE_DAT_RCV_ITM03 | NVARCHAR2(100) | Y | Receive Item 03 / 受信項目03 |
| 27 | EE_DAT_RCV_ITM04 | NVARCHAR2(100) | Y | Receive Item 04 / 受信項目04 |
| 28 | EE_DAT_RCV_ITM05 | NVARCHAR2(100) | Y | Receive Item 05 / 受信項目05 |
| 29 | EE_DAT_RCV_ITM06 | NVARCHAR2(100) | Y | Receive Item 06 / 受信項目06 |
| 30 | EE_DAT_RCV_ITM07 | NVARCHAR2(100) | Y | Receive Item 07 / 受信項目07 |
| 31 | EE_DAT_RCV_ITM08 | NVARCHAR2(100) | Y | Receive Item 08 / 受信項目08 |
| 32 | EE_DAT_RCV_ITM09 | NVARCHAR2(100) | Y | Receive Item 09 / 受信項目09 |
| 33 | EE_DAT_RCV_ITM10 | NVARCHAR2(100) | Y | Receive Item 10 / 受信項目10 |
| 34 | EE_DAT_RCV_ITM11 | NVARCHAR2(100) | Y | Receive Item 11 / 受信項目11 |
| 35 | EE_DAT_RCV_ITM12 | NVARCHAR2(100) | Y | Receive Item 12 / 受信項目12 |
| 36 | EE_DAT_RCV_ITM13 | NVARCHAR2(100) | Y | Receive Item 13 / 受信項目13 |
| 37 | EE_DAT_RCV_ITM14 | NVARCHAR2(100) | Y | Receive Item 14 / 受信項目14 |
| 38 | EE_DAT_RCV_ITM15 | NVARCHAR2(100) | Y | Receive Item 15 / 受信項目15 |
| 39 | EE_DAT_RCV_ITM16 | NVARCHAR2(100) | Y | Receive Item 16 / 受信項目16 |
| 40 | EE_DAT_RCV_ITM17 | NVARCHAR2(100) | Y | Receive Item 17 / 受信項目17 |
| 41 | EE_DAT_RCV_ITM18 | NVARCHAR2(100) | Y | Receive Item 18 / 受信項目18 |
| 42 | EE_DAT_RCV_ITM19 | NVARCHAR2(100) | Y | Receive Item 19 / 受信項目19 |
| 43 | EE_DAT_RCV_ITM20 | NVARCHAR2(100) | Y | Receive Item 20 / 受信項目20 |
| 44 | EE_DAT_RCV_ITM21 | NVARCHAR2(100) | Y | Receive Item 21 / 受信項目21 |
| 45 | EE_DAT_RCV_ITM22 | NVARCHAR2(100) | Y | Receive Item 22 / 受信項目22 |
| 46 | EE_DAT_RCV_ITM23 | NVARCHAR2(100) | Y | Receive Item 23 / 受信項目23 |
| 47 | EE_DAT_RCV_ITM24 | NVARCHAR2(100) | Y | Receive Item 24 / 受信項目24 |
| 48 | EE_DAT_RCV_ITM25 | NVARCHAR2(100) | Y | Receive Item 25 / 受信項目25 |
| 49 | EE_DAT_RCV_ITM26 | NVARCHAR2(100) | Y | Receive Item 26 / 受信項目26 |
| 50 | EE_DAT_RCV_ITM27 | NVARCHAR2(100) | Y | Receive Item 27 / 受信項目27 |
| 51 | EE_DAT_RCV_ITM28 | NVARCHAR2(100) | Y | Receive Item 28 / 受信項目28 |
| 52 | EE_DAT_RCV_ITM29 | NVARCHAR2(100) | Y | Receive Item 29 / 受信項目29 |
| 53 | EE_DAT_RCV_ITM30 | NVARCHAR2(100) | Y | Receive Item 30 / 受信項目30 |
| 54 | EE_DAT_RCV_ITM31 | NVARCHAR2(100) | Y | Receive Item 31 / 受信項目31 |
| 55 | EE_DAT_RCV_ITM32 | NVARCHAR2(100) | Y | Receive Item 32 / 受信項目32 |
| 56 | EE_DAT_RCV_ITM33 | NVARCHAR2(100) | Y | Receive Item 33 / 受信項目33 |
| 57 | EE_DAT_RCV_ITM34 | NVARCHAR2(100) | Y | Receive Item 34 / 受信項目34 |
| 58 | EE_DAT_RCV_ITM35 | NVARCHAR2(100) | Y | Receive Item 35 / 受信項目35 |
| 59 | EE_DAT_RCV_ITM36 | NVARCHAR2(100) | Y | Receive Item 36 / 受信項目36 |
| 60 | EE_DAT_RCV_ITM37 | NVARCHAR2(100) | Y | Receive Item 37 / 受信項目37 |
| 61 | EE_DAT_RCV_ITM38 | NVARCHAR2(100) | Y | Receive Item 38 / 受信項目38 |
| 62 | EE_DAT_RCV_ITM39 | NVARCHAR2(100) | Y | Receive Item 39 / 受信項目39 |
| 63 | EE_DAT_RCV_ITM40 | NVARCHAR2(100) | Y | Receive Item 40 / 受信項目40 |
| 64 | EE_DAT_RCV_ITM41 | NVARCHAR2(100) | Y | Receive Item 41 / 受信項目41 |
| 65 | EE_DAT_RCV_ITM42 | NVARCHAR2(100) | Y | Receive Item 42 / 受信項目42 |
| 66 | EE_DAT_RCV_ITM43 | NVARCHAR2(100) | Y | Receive Item 43 / 受信項目43 |
| 67 | EE_DAT_RCV_ITM44 | NVARCHAR2(100) | Y | Receive Item 44 / 受信項目44 |
| 68 | EE_DAT_RCV_ITM45 | NVARCHAR2(100) | Y | Receive Item 45 / 受信項目45 |
| 69 | EE_DAT_RCV_ITM46 | NVARCHAR2(100) | Y | Receive Item 46 / 受信項目46 |
| 70 | EE_DAT_RCV_ITM47 | NVARCHAR2(100) | Y | Receive Item 47 / 受信項目47 |
| 71 | EE_DAT_RCV_ITM48 | NVARCHAR2(100) | Y | Receive Item 48 / 受信項目48 |
| 72 | EE_DAT_RCV_ITM49 | NVARCHAR2(100) | Y | Receive Item 49 / 受信項目49 |
| 73 | EE_DAT_RCV_ITM50 | NVARCHAR2(100) | Y | Receive Item 50 / 受信項目50 |
| 74 | EE_DAT_RCV_ITM51 | NVARCHAR2(100) | Y | Receive Item 51 / 受信項目51 |
| 75 | EE_DAT_RCV_ITM52 | NVARCHAR2(100) | Y | Receive Item 52 / 受信項目52 |
| 76 | EE_DAT_RCV_ITM53 | NVARCHAR2(100) | Y | Receive Item 53 / 受信項目53 |
| 77 | EE_DAT_RCV_ITM54 | NVARCHAR2(100) | Y | Receive Item 54 / 受信項目54 |
| 78 | EE_DAT_RCV_ITM55 | NVARCHAR2(100) | Y | Receive Item 55 / 受信項目55 |
| 79 | EE_DAT_RCV_ITM56 | NVARCHAR2(100) | Y | Receive Item 56 / 受信項目56 |
| 80 | EE_DAT_RCV_ITM57 | NVARCHAR2(100) | Y | Receive Item 57 / 受信項目57 |
| 81 | EE_DAT_RCV_ITM58 | NVARCHAR2(100) | Y | Receive Item 58 / 受信項目58 |
| 82 | EE_DAT_RCV_ITM59 | NVARCHAR2(100) | Y | Receive Item 59 / 受信項目59 |
| 83 | EE_DAT_RCV_ITM60 | NVARCHAR2(100) | Y | Receive Item 60 / 受信項目60 |
| 84 | EE_DAT_RCV_ITM61 | NVARCHAR2(100) | Y | Receive Item 61 / 受信項目61 |
| 85 | EE_DAT_RCV_ITM62 | NVARCHAR2(100) | Y | Receive Item 62 / 受信項目62 |
| 86 | EE_DAT_RCV_ITM63 | NVARCHAR2(100) | Y | Receive Item 63 / 受信項目63 |
| 87 | EE_DAT_RCV_ITM64 | NVARCHAR2(100) | Y | Receive Item 64 / 受信項目64 |
| 88 | EE_DAT_RCV_ITM65 | NVARCHAR2(100) | Y | Receive Item 65 / 受信項目65 |
| 89 | EE_DAT_RCV_ITM66 | NVARCHAR2(100) | Y | Receive Item 66 / 受信項目66 |
| 90 | EE_DAT_RCV_ITM67 | NVARCHAR2(100) | Y | Receive Item 67 / 受信項目67 |
| 91 | EE_DAT_RCV_ITM68 | NVARCHAR2(100) | Y | Receive Item 68 / 受信項目68 |
| 92 | EE_DAT_RCV_ITM69 | NVARCHAR2(100) | Y | Receive Item 69 / 受信項目69 |
| 93 | EE_DAT_RCV_ITM70 | NVARCHAR2(100) | Y | Receive Item 70 / 受信項目70 |
| 94 | EE_DAT_RCV_NUM_ITM01 | NUMBER(14,4) | Y | Receive Number Item 01 / 受信数値項目01 |
| 95 | EE_DAT_RCV_NUM_ITM02 | NUMBER(14,4) | Y | Receive Number Item 02 / 受信数値項目02 |
| 96 | EE_DAT_RCV_NUM_ITM03 | NUMBER(14,4) | Y | Receive Number Item 03 / 受信数値項目03 |
| 97 | EE_DAT_RCV_NUM_ITM04 | NUMBER(14,4) | Y | Receive Number Item 04 / 受信数値項目04 |
| 98 | EE_DAT_RCV_NUM_ITM05 | NUMBER(14,4) | Y | Receive Number Item 05 / 受信数値項目05 |
| 99 | EE_DAT_RCV_NUM_ITM06 | NUMBER(14,4) | Y | Receive Number Item 06 / 受信数値項目06 |
| 100 | EE_DAT_RCV_NUM_ITM07 | NUMBER(14,4) | Y | Receive Number Item 07 / 受信数値項目07 |
| 101 | EE_DAT_RCV_NUM_ITM08 | NUMBER(14,4) | Y | Receive Number Item 08 / 受信数値項目08 |
| 102 | EE_DAT_RCV_NUM_ITM09 | NUMBER(14,4) | Y | Receive Number Item 09 / 受信数値項目09 |
| 103 | EE_DAT_RCV_NUM_ITM10 | NUMBER(14,4) | Y | Receive Number Item 10 / 受信数値項目10 |
| 104 | EE_DAT_RCV_NUM_ITM11 | NUMBER(14,4) | Y | Receive Number Item 11 / 受信数値項目11 |
| 105 | EE_DAT_RCV_NUM_ITM12 | NUMBER(14,4) | Y | Receive Number Item 12 / 受信数値項目12 |
| 106 | EE_DAT_RCV_NUM_ITM13 | NUMBER(14,4) | Y | Receive Number Item 13 / 受信数値項目13 |
| 107 | EE_DAT_RCV_NUM_ITM14 | NUMBER(14,4) | Y | Receive Number Item 14 / 受信数値項目14 |
| 108 | EE_DAT_RCV_NUM_ITM15 | NUMBER(14,4) | Y | Receive Number Item 15 / 受信数値項目15 |
| 109 | EE_DAT_RCV_NUM_ITM16 | NUMBER(14,4) | Y | Receive Number Item 16 / 受信数値項目16 |
| 110 | EE_DAT_RCV_NUM_ITM17 | NUMBER(14,4) | Y | Receive Number Item 17 / 受信数値項目17 |
| 111 | EE_DAT_RCV_NUM_ITM18 | NUMBER(14,4) | Y | Receive Number Item 18 / 受信数値項目18 |
| 112 | EE_DAT_RCV_NUM_ITM19 | NUMBER(14,4) | Y | Receive Number Item 19 / 受信数値項目19 |
| 113 | EE_DAT_RCV_NUM_ITM20 | NUMBER(14,4) | Y | Receive Number Item 20 / 受信数値項目20 |
| 114 | EE_DAT_RCV_RMS_ITM01 | NVARCHAR2(1000) | Y | Receive Remarks Item 01 / 受信備考項目01 |
| 115 | EE_DAT_RCV_RMS_ITM02 | NVARCHAR2(1000) | Y | Receive Remarks Item 02 / 受信備考項目02 |
| 116 | EE_DAT_RCV_RMS_ITM03 | NVARCHAR2(1000) | Y | Receive Remarks Item 03 / 受信備考項目03 |
| 117 | EE_DAT_RCV_RMS_ITM04 | NVARCHAR2(1000) | Y | Receive Remarks Item 04 / 受信備考項目04 |
| 118 | EE_DAT_RCV_RMS_ITM05 | NVARCHAR2(1000) | Y | Receive Remarks Item 05 / 受信備考項目05 |
| 119 | EE_DAT_RCV_RMS_ITM06 | NVARCHAR2(1000) | Y | Receive Remarks Item 06 / 受信備考項目06 |
| 120 | EE_DAT_RCV_RMS_ITM07 | NVARCHAR2(1000) | Y | Receive Remarks Item 07 / 受信備考項目07 |
| 121 | EE_DAT_RCV_RMS_ITM08 | NVARCHAR2(1000) | Y | Receive Remarks Item 08 / 受信備考項目08 |
| 122 | EE_DAT_RCV_RMS_ITM09 | NVARCHAR2(1000) | Y | Receive Remarks Item 09 / 受信備考項目09 |
| 123 | EE_DAT_RCV_RMS_ITM10 | NVARCHAR2(1000) | Y | Receive Remarks Item 10 / 受信備考項目10 |
