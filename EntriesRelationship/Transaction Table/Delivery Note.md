# GWH_TJ_DVN
#Entity #Standard #OUTBOUND

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
| 18 | DVN_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | DVN_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | DVN_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | DVN_DLCT_NUM | CHAR(14) | N | Delivery Control Number / 配送管理番号 |
| 22 | DVN_DLCT_LN_NUM | NUMBER(3) | N | Delivery Control Line Number / 配送管理行番号 |
| 23 | DVN_DN_NUM | NVARCHAR2(80) | Y | Delivery Note Number / 送状番号 |
| 24 | DVN_PC_NUM | NVARCHAR2(12) | Y | Packing Number / 梱包番号 |
| 25 | DVN_PCLN_NUM | NUMBER(4) | Y | Packing Line Number / 梱包ラインNo |
| 26 | DVN_PRT_YMD | CHAR(8) | Y | Printed Date / 印刷日 |
| 27 | DVN_DCP_YMD | CHAR(8) | Y | Delivered Completed Date / 配達完了日 |
| 28 | DVN_DCP_TIM | CHAR(4) | Y | Delivered Completed Time / 配達完了時間 |
| 29 | DVN_SLPR_FLG | CHAR(1) | Y | Shipping Label Printed Flag / 荷札印刷フラグ |
| 30 | DVN_DNPR_FLG | CHAR(1) | Y | Delivery Note Printed Flag / 送り状発行フラグ |
| 31 | DVN_SZWT_KND | NVARCHAR2(10) | Y | Size / Weight Kind / サイズ重量区分 |
| 32 | DVN_CSD_AMN | NUMBER(14,4) | Y | Cash on Delivery Commodity Price / 代引品代 |
