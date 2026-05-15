# GWH_TM_DOCS
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
| 18 | DOCS_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | DOCS_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | DOCS_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | DOCS_DT_KND | CHAR(2) | N | Data Kind Code / データ種別コード |
| 22 | DOCS_DOUT_SEQ | NUMBER(3) | N | Sequence No. / シーケンスNo. |
| 23 | DOCS_DT_CMBN_ORD | NUMBER(2) | N | Data Combined order / データ結合順 |
| 24 | DOCS_TBL_COD | NVARCHAR2(100) | Y | Table ID / テーブルID |
| 25 | DOCS_TBL_NAM | NVARCHAR2(60) | Y | Table Name / テーブル名 |
| 26 | DOCS_FLD_COD | NVARCHAR2(100) | Y | Field ID / フィールドID |
| 27 | DOCS_FLD_NAM | NVARCHAR2(400) | Y | Field Name / フィールド名 |
| 28 | DOCS_DT_RD_SPSTN | NUMBER(3) | Y | Data Read Start Position / データ読込み開始位置 |
| 29 | DOCS_DT_SZ | NUMBER(3) | Y | Data Size / データサイズ |
| 30 | DOCS_FXD_VL | NVARCHAR2(400) | Y | Fixed Value / 固定値 |
