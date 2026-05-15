# GWH_TJ_IF_SRL
#Entity #Standard #HISTORY

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
| 18 | ISRL_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | ISRL_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | ISRL_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | ISRL_AS_NUM | NVARCHAR2(12) | N | Arrival and Shipping Number / 入出荷番号 |
| 22 | ISRL_ASLN_NUM | NUMBER(4) | N | Arrival and Shipping Line Number / 入出荷ラインNo |
| 23 | ISRL_ASSQ_NUM | NUMBER(4) | N | Arrival and Shipping Seq Number / 入出荷SEQNo |
| 24 | ISRL_SEQ_NUM | NUMBER(38) | N | Process SEQ number / 処理SEQ番号 |
| 25 | ISRL_SRL_NUM | NVARCHAR2(100) | N | Serial number / シリアル番号 |
| 26 | ISRL_AV_NUM | NVARCHAR2(12) | N | Arrival Number / 入荷番号 |
| 27 | ISRL_AVLN_NUM | NUMBER(4) | N | Arrival Line No / 入荷ラインNo |
| 28 | ISRL_AVSQ_NUM | NUMBER(4) | N | Arrival Seq No / 入荷SEQNo |
| 29 | ISRL_PROD_COD | NVARCHAR2(100) | N | Product Code / 商品コード |
| 30 | ISRL_ORGN_COD | CHAR(2) | Y | Country Of Origin / 原産国 |
| 31 | ISRL_SSR_INNO | NVARCHAR2(100) | Y | Receiving slip No / 入庫伝票No |
