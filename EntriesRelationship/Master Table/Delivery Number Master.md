# GWH_TM_DNUM
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
| 18 | DNUM_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | DNUM_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | DNUM_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | DNUM_DN_KND | NVARCHAR2(10) | N | Delivery Note Kind / 送状種類 |
| 22 | DNUM_LBL_COD | NVARCHAR2(200) | N | Label Code / ラベルコード |
| 23 | DNUM_RNG_KND | CHAR(1) | N | Range Kind / レンジ区分 |
| 24 | DNUM_PRFX_COD | NVARCHAR2(6) | Y | Prefix / プレフィックス |
| 25 | DNUM_STR_NUM1 | NVARCHAR2(60) | N | Start No1 / 開始番号1 |
| 26 | DNUM_END_NUM1 | NVARCHAR2(60) | N | End No1 / 終了番号1 |
| 27 | DNUM_CUR_NUM1 | NVARCHAR2(60) | N | Current No1 / 現在番号1 |
| 28 | DNUM_STR_NUM2 | NVARCHAR2(60) | Y | Start No2 / 開始番号2 |
| 29 | DNUM_END_NUM2 | NVARCHAR2(60) | Y | End No2 / 終了番号2 |
| 30 | DNUM_CUR_NUM2 | NVARCHAR2(60) | Y | Current No2 / 現在番号2 |
| 31 | DNUM_NUM_KND | NVARCHAR2(2) | N | Numbering Type / 採番タイプ |
| 32 | DNUM_NNUM_FLG1 | CHAR(1) | N | No1 Non Numbering Flag / 番号1採番不可フラグ |
| 33 | DNUM_NNUM_FLG2 | CHAR(1) | N | No2 Non Numbering Flag / 番号2採番不可フラグ |
| 34 | DNUM_NLOP_FLG | NVARCHAR2(2) | N | Non Loop Flag / ループ不可フラグ |
| 35 | DNUM_CD_KND | CHAR(2) | N | Check digit Kind / チェックデジット区分 |
| 36 | DNUM_RMKS | NVARCHAR2(100) | Y | Remarks / 備考 |
| 37 | DNUM_USE_RATE | NUMBER(2) | Y | Number Usage Rate / 番号使用率 |
