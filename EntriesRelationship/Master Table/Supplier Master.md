# GWH_TM_SPL
#Entity #Standard #MASTER

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
| 18 | SPL_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | SPL_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | SPL_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | SPL_COD | NVARCHAR2(80) | N | Supplier Code / 納入元コード |
| 22 | SPL_NAM1 | NVARCHAR2(100) | Y | Supplier Name1st / 納入元名称1st |
| 23 | SPL_NAM2 | NVARCHAR2(100) | Y | Supplier Name2nd / 納入元名称2nd |
| 24 | SPL_TEL | NVARCHAR2(40) | Y | Supplier Tel / 納入元電話番号 |
| 25 | SPL_ZIP | NVARCHAR2(20) | Y | Supplier Zip / 納入元郵便番号 |
| 26 | SPL_JIS | NVARCHAR2(22) | Y | Supplier Jis / 納入元住所コード |
| 27 | SPL_ADR1 | NVARCHAR2(100) | Y | Supplier Address1 / 納入元住所1 |
| 28 | SPL_ADR2 | NVARCHAR2(100) | Y | Supplier Address2 / 納入元住所2 |
| 29 | SPL_ADR3 | NVARCHAR2(100) | Y | Supplier Address3 / 納入元住所3 |
| 30 | SPL_ADR4 | NVARCHAR2(100) | Y | Supplier Address4 / 納入元住所4 |
| 31 | SPL_ADR5 | NVARCHAR2(100) | Y | Supplier Address5 / 納入元住所5 |
| 32 | SPL_DPRT_NAM | NVARCHAR2(100) | Y | Supplier Department Name / 納入元部署名 |
| 33 | SPL_REP_NAM | NVARCHAR2(100) | Y | Supplier Representative / 納入元担当者 |
| 34 | SPL_MAL | NVARCHAR2(200) | Y | Supplier Mail / 納入元メールアドレス |
| 35 | SPL_VAT_COD | CHAR(2) | Y | Vat Registration Country / VAT_REGISTRATION_COUNTRY |
| 36 | SPL_VAT_NUM | CHAR(15) | Y | Vat Registration No / VAT_REGISTRATION_NO. |
